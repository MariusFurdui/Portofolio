library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity ElevatorController is
    Port ( 
        CLK100MHZ   : in STD_LOGIC;
        BTNC        : in STD_LOGIC;  -- reset
        BTNL        : in STD_LOGIC;  -- senzor usa
        BTNR        : in STD_LOGIC;  -- senzor greutate
        SW          : in STD_LOGIC_VECTOR(7 downto 0);  -- primele 4 pentru etaj, SW5 si SW4 pentru requests internal(01), external up (10), external down(11), SW6 submit request, SW7 pt viteza
        LED         : out STD_LOGIC_VECTOR(15 downto 0); -- afisare etaj curent; 14 door open, 15 sus/jos
        SEG7_CATH   : out STD_LOGIC_VECTOR(6 downto 0); -- afisare etaj, stare
        AN          : out STD_LOGIC_VECTOR(7 downto 0) -- selectat afisor
    );
end ElevatorController;

architecture Behavioral of ElevatorController is
    -- definire stari
    type elevator_state_type is (IDLE, MOVING_UP, MOVING_DOWN, DOOR_OPENING, DOOR_CLOSING, WAITING);
    signal current_state, next_state : elevator_state_type;

    -- etaj curent si etaj cerut
    signal current_floor : integer range 0 to 12 := 0;  -- incepe la parter
    signal target_floor : integer range 0 to 12 := 0;
    
    -- directie
    signal going_up : boolean := true;
    
    -- urmaresc cererile
    type request_array is array (0 to 12) of boolean;
    signal internal_requests : request_array := (others => false);
    signal external_up_requests : request_array := (others => false);
    signal external_down_requests : request_array := (others => false);
    
    -- pentru etaj vizitat deja
    signal floor_serviced : boolean := false;
    
    -- timere
    constant CLOCK_FREQ : integer := 100000000;  -- 100MHz clock
    constant ONE_SEC : integer := CLOCK_FREQ;
    signal door_timer : integer range 0 to 10*ONE_SEC := 0;
    signal floor_timer : integer range 0 to 3*ONE_SEC := 0;
    signal door_time : integer range 1 to 10 := 3;  -- 3 secunde prestabilit
    
    -- timp miscare etaj
    signal floor_time : integer;
    
    -- debouncer btn
    signal btn_debounce_counter : integer range 0 to 1000000 := 0;
    signal debounced_sw : STD_LOGIC_VECTOR(7 downto 0) := (others => '0');
    signal prev_sw : STD_LOGIC_VECTOR(7 downto 0) := (others => '0');
    
    -- semnale SSD
    signal display_value : integer range 0 to 15;
    signal digit_select : integer range 0 to 7 := 0;
    signal digit_timer : integer range 0 to 100000 := 0;
    
    -- status usa greutate
    signal door_clear : STD_LOGIC;
    signal weight_ok : STD_LOGIC;
    
    -- etaje in binar si tipul cererii
    signal request_valid : boolean := false;
    signal request_floor : integer range 0 to 15 := 0;
    signal request_type : std_logic_vector(1 downto 0) := "00";
    
    -- constante pentru tip cereri
    constant REQ_NONE : std_logic_vector(1 downto 0) := "00";
    constant REQ_INTERNAL : std_logic_vector(1 downto 0) := "01";
    constant REQ_EXT_UP : std_logic_vector(1 downto 0) := "10";
    constant REQ_EXT_DOWN : std_logic_vector(1 downto 0) := "11";
    
    -- transformare int->SSD
    function int_to_7seg(digit : integer) return STD_LOGIC_VECTOR is
    begin
        case digit is
            when 0 => return "1000000"; -- 0
            when 1 => return "1111001"; -- 1
            when 2 => return "0100100"; -- 2
            when 3 => return "0110000"; -- 3
            when 4 => return "0011001"; -- 4
            when 5 => return "0010010"; -- 5
            when 6 => return "0000010"; -- 6
            when 7 => return "1111000"; -- 7
            when 8 => return "0000000"; -- 8
            when 9 => return "0010000"; -- 9
            when 10 => return "0001000"; -- A
            when 11 => return "0000011"; -- b
            when 12 => return "1000110"; -- C
            when others => return "0111111"; -- linie
        end case;
    end function;
    
    -- verific cereri in asteptare
    function has_requests(internal_req, ext_up_req, ext_dn_req : request_array) return boolean is
        variable has_req : boolean := false;
    begin
        for i in 0 to 12 loop
            if internal_req(i) then
                has_req := true;
                exit;
            end if;
        end loop;
        
        if not has_req then
            for i in 0 to 12 loop
                if (i < 12 and ext_up_req(i)) or (i > 0 and ext_dn_req(i)) then
                    has_req := true;
                    exit;
                end if;
            end loop;
        end if;
        
        return has_req;
    end function;
    
    -- verific daca am o cerere si sunt in trecere in sus
    function should_stop_going_up(
        current_flr : integer;
        internal_req, ext_up_req : request_array) return boolean is
    begin
        return internal_req(current_flr) or (current_flr < 12 and ext_up_req(current_flr));
    end function;
    
    -- verific daca am o cerere si sunt in trecere in jos
    function should_stop_going_down(
        current_flr : integer;
        internal_req, ext_dn_req : request_array) return boolean is
    begin
        return internal_req(current_flr) or (current_flr > 0 and ext_dn_req(current_flr));
    end function;
    
    -- determin directia si etaj cerut
    procedure determine_direction_and_target(
        current_flr : in integer;
        internal_req : in request_array;
        ext_up_req : in request_array;
        ext_dn_req : in request_array;
        direction : inout boolean;
        target : out integer) is
        variable found : boolean := false;
    begin
        -- verific daca trebuie sa continuam in aceeasi directie
        if direction then
            -- urc, verific pt cereri mai sus
            for i in 0 to 12 loop
                if i > current_flr then
                    if internal_req(i) or (i < 12 and ext_up_req(i)) then
                        target := i;
                        found := true;
                        exit;
                    end if;
                end if;
            end loop;
            
            -- daca nu, verific pt cereri mai jos
            if not found then
                direction := false;  -- schimb directia
                for i in 12 downto 0 loop
                if i < current_flr then
                        if internal_req(i) or (i > 0 and ext_dn_req(i)) then
                            target := i;
                            found := true;
                            exit;
                        end if;
                    end if;
                end loop;
            end if;
        else
            -- cobor, verific pentru cereri mai jos
            for i in 12 downto 0 loop
            if i < current_flr then
                    if internal_req(i) or (i > 0 and ext_dn_req(i)) then
                        target := i;
                        found := true;
                        exit;
                    end if;
                end if;
            end loop;
            
            -- daca nu, verific pt cereri mai sus
            if not found then
                direction := true;  -- schimb directia
                for i in 0 to 12 loop
                if i > current_flr then
                        if internal_req(i) or (i < 12 and ext_up_req(i)) then
                            target := i;
                            found := true;
                            exit;
                        end if;
                    end if;
                end loop;
            end if;
        end if;
        
        -- daca nu am cerere, ma duc la parter
        if not found then
            target := current_flr;
        end if;
    end procedure;
    
begin
    -- schimb logica butoanelor
    door_clear <= not BTNL;  -- usa libera cand buton nu e apasat
    weight_ok <= not BTNR;   -- greutate OK cand buton nu e apasat
    
    -- setez timp miscare in functie de viteza
    floor_time <= ONE_SEC when SW(7) = '0' else 3*ONE_SEC;
    
    -- procesare etaj cerut si tip cerere
    process(CLK100MHZ)
        variable floor_num : integer;
    begin
        if rising_edge(CLK100MHZ) then
            -- valori default
            request_valid <= false;
            
            if btn_debounce_counter = 1000000 then  -- 10ms debounce time
                debounced_sw <= SW;
                btn_debounce_counter <= 0;
            else
                btn_debounce_counter <= btn_debounce_counter + 1;
            end if;
            
            -- SW(3 downto 0) = etaj in binar (0-12)
            -- SW(5 downto 4) = tip cerere (00=none, 01=intern, 10=ext up, 11=ext down)
            -- SW(6) = submit
            
            -- din binar in int
            floor_num := to_integer(unsigned(debounced_sw(3 downto 0)));
            
            -- detectez submit
            if debounced_sw(6) = '1' and prev_sw(6) = '0' then
                -- etaj
                if floor_num <= 12 then
                    -- tip cerere
                    request_type <= debounced_sw(5 downto 4);
                    
                    -- validez cerere in fct de etaj
                    if (debounced_sw(5 downto 4) = REQ_INTERNAL) or
                       (debounced_sw(5 downto 4) = REQ_EXT_UP and floor_num < 12) or
                       (debounced_sw(5 downto 4) = REQ_EXT_DOWN and floor_num > 0) then
                        request_valid <= true;
                        request_floor <= floor_num;
                    end if;
                end if;
            end if;
            
            prev_sw <= debounced_sw;
        end if;
    end process;
    
    -- update uri
    process(CLK100MHZ, BTNC)
    begin
        if BTNC = '1' then
            -- reset cereri
            internal_requests <= (others => false);
            external_up_requests <= (others => false);
            external_down_requests <= (others => false);
        elsif rising_edge(CLK100MHZ) then
            -- ma ocup de cereri noi
            if request_valid then
                case request_type is
                    when REQ_INTERNAL =>
                        internal_requests(request_floor) <= true;
                    
                    when REQ_EXT_UP =>
                        if request_floor < 12 then
                            external_up_requests(request_floor) <= true;
                        end if;
                    
                    when REQ_EXT_DOWN =>
                        if request_floor > 0 then
                            external_down_requests(request_floor) <= true;
                        end if;
                    
                    when others =>
                        -- nimic pentru REQ_NONE
                end case;
            end if;
            
            -- reset cerere cand e valida
            if current_state = WAITING then
                internal_requests(current_floor) <= false;
                if going_up then
                    external_up_requests(current_floor) <= false;
                else
                    external_down_requests(current_floor) <= false;
                end if;
            end if;
        end if;
    end process;
    
    -- registru stare si stare urm
    process(CLK100MHZ, BTNC)
    begin
        if BTNC = '1' then
            current_state <= IDLE;
            current_floor <= 0;
            door_timer <= 0;
            floor_timer <= 0;
        elsif rising_edge(CLK100MHZ) then
            current_state <= next_state;
            
            case current_state is
                when IDLE =>
                    -- nu fac nimic
                
                when MOVING_UP =>
                    if floor_timer >= floor_time then
                        floor_timer <= 0;
                        current_floor <= current_floor + 1;
                    else
                        floor_timer <= floor_timer + 1;
                    end if;
                
                when MOVING_DOWN =>
                    if floor_timer >= floor_time then
                        floor_timer <= 0;
                        current_floor <= current_floor - 1;
                    else
                        floor_timer <= floor_timer + 1;
                    end if;
                
                when DOOR_OPENING =>
                    -- reset timer usa cand e deschisa complet
                    door_timer <= 0;
                
                when DOOR_CLOSING =>
                    -- nu fac nimic
                
                when WAITING =>
                    if door_timer >= door_time * ONE_SEC then
                        door_timer <= 0;
                    else
                        door_timer <= door_timer + 1;
                    end if;
            end case;
        end if;
    end process;
    
    -- stare urm
    process(current_state, current_floor, internal_requests, external_up_requests, 
            external_down_requests, door_timer, floor_timer, weight_ok, door_clear, going_up, target_floor, door_time)
        variable v_going_up : boolean;
        variable v_target_floor : integer range 0 to 12;
    begin
        next_state <= current_state;
        
        case current_state is
            when IDLE =>
                -- verific daca exista cereri
                if has_requests(internal_requests, external_up_requests, external_down_requests) then
                    -- initializez variabile cu val semnale
                    v_going_up := going_up;
                    v_target_floor := 0;
                    
                    -- apelez functia cu variabilele
                    determine_direction_and_target(
                        current_floor, 
                        internal_requests, 
                        external_up_requests, 
                        external_down_requests,
                        v_going_up, 
                        v_target_floor
                    );
                    
                    -- folosesc variabilele pentru tranzitia de stari
                    if v_target_floor > current_floor then
                        next_state <= MOVING_UP;
                    elsif v_target_floor < current_floor then
                        next_state <= MOVING_DOWN;
                    else
                        next_state <= DOOR_OPENING;
                    end if;
                end if;
            
            when MOVING_UP =>
                if current_floor = target_floor then
                    next_state <= DOOR_OPENING;
                elsif should_stop_going_up(current_floor, internal_requests, external_up_requests) then
                    next_state <= DOOR_OPENING;
                end if;
            
            when MOVING_DOWN =>
                if current_floor = target_floor then
                    next_state <= DOOR_OPENING;
                elsif should_stop_going_down(current_floor, internal_requests, external_down_requests) then
                    next_state <= DOOR_OPENING;
                end if;
            
            when DOOR_OPENING =>
                next_state <= WAITING;
            
            when WAITING =>
                if door_timer >= door_time * ONE_SEC then
                    next_state <= DOOR_CLOSING;
                end if;
            
            when DOOR_CLOSING =>
                if door_clear = '0' then
                    next_state <= DOOR_OPENING;  -- obstacol usa
                elsif weight_ok = '0' then
                    next_state <= WAITING;  -- greutate depasita
                else
                    next_state <= IDLE;
                end if;
        end case;
    end process;
    
    -- update etaj cerut
    process(CLK100MHZ)
        variable v_going_up : boolean;
        variable v_target_floor : integer range 0 to 12;
    begin
        if rising_edge(CLK100MHZ) then
            if current_state = IDLE and has_requests(internal_requests, external_up_requests, external_down_requests) then
                -- initializez variabile cu val semnale
                v_going_up := going_up;
                v_target_floor := 0;
                
                -- apelez functie cu variabilele
                determine_direction_and_target(
                    current_floor, 
                    internal_requests, 
                    external_up_requests, 
                    external_down_requests,
                    v_going_up, 
                    v_target_floor
                );
                
                -- update semnale
                going_up <= v_going_up;
                target_floor <= v_target_floor;
            end if;
        end if;
    end process;
    
    -- output logic leduri
    process(current_state, current_floor, going_up)
    begin
        -- default oprite
        LED <= (others => '0');
        
        -- pentru etaj curent
        if current_floor <= 12 then
            LED(current_floor) <= '1';
        end if;
        
        -- status usa
        if current_state = DOOR_OPENING or current_state = WAITING then
            LED(14) <= '1';  -- deschisa
        end if;
        
        -- directie
        if current_state = MOVING_UP then
            LED(15) <= '1';  -- sus
        elsif current_state = MOVING_DOWN then
            LED(15) <= '0';  -- jos (oprit)
        end if;
    end process;
    
    -- 7-segment display control
    process(CLK100MHZ)
    begin
        if rising_edge(CLK100MHZ) then
            -- actualizez cifra
            if digit_timer = 100000 then
                digit_timer <= 0;
                
                -- rotire cifre
                if digit_select = 7 then
                    digit_select <= 0;
                else
                    digit_select <= digit_select + 1;
                end if;
            else
                digit_timer <= digit_timer + 1;
            end if;
            
            -- setez cifra folosita
            AN <= (others => '1');  -- toate oprite default
            AN(digit_select) <= '0';  -- curent
            
            -- setez cifra afisata
            case digit_select is
                when 0 =>
                    -- afisez etaj curent
                    SEG7_CATH <= int_to_7seg(current_floor);
                when 1 =>
                    -- afisez E de la ETAJ
                    SEG7_CATH <= "0000110";  -- E
                when 2 =>
                    -- afisez stare elevator
                    case current_state is
                        when IDLE => SEG7_CATH <= "1001111";  -- I (IDLE)
                        when MOVING_UP => SEG7_CATH <= "1000001";  -- U (Up)
                        when MOVING_DOWN => SEG7_CATH <= "0100001";  -- d (down)
                        when DOOR_OPENING | WAITING | DOOR_CLOSING => SEG7_CATH <= "0100011";  -- o (open)
                        when others => SEG7_CATH <= "0111111";  -- - (linie)
                    end case;
                when 3 =>
                    -- afisez etaj selectat
                    SEG7_CATH <= int_to_7seg(to_integer(unsigned(debounced_sw(3 downto 0))));
                when 4 =>
                    -- afisez tip cerere
                    case debounced_sw(5 downto 4) is
                        when REQ_INTERNAL => SEG7_CATH <= "1001111";  -- I (Internal)
                        when REQ_EXT_UP => SEG7_CATH <= "1000001";    -- U (Up)
                        when REQ_EXT_DOWN => SEG7_CATH <= "0100001";  -- d (Down)
                        when others => SEG7_CATH <= "1111111";        -- gol
                    end case;
                when others =>
                    SEG7_CATH <= "1111111";  -- gol
            end case;
        end if;
    end process;
    
end Behavioral;