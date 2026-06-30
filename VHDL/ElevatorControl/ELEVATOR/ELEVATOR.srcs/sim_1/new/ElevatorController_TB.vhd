library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity ElevatorController_tb is
end ElevatorController_tb;

architecture Behavioral of ElevatorController_tb is
    component ElevatorController
        Port ( 
            CLK100MHZ   : in STD_LOGIC;
            BTNC        : in STD_LOGIC;
            BTNL        : in STD_LOGIC;
            BTNR        : in STD_LOGIC;
            SW          : in STD_LOGIC_VECTOR(7 downto 0);
            LED         : out STD_LOGIC_VECTOR(15 downto 0);
            SEG7_CATH   : out STD_LOGIC_VECTOR(6 downto 0);
            AN          : out STD_LOGIC_VECTOR(7 downto 0)
        );
    end component;

    signal CLK100MHZ   : STD_LOGIC := '0';
    signal BTNC        : STD_LOGIC := '0';
    signal BTNL        : STD_LOGIC := '0';
    signal BTNR        : STD_LOGIC := '0';
    signal SW          : STD_LOGIC_VECTOR(7 downto 0) := (others => '0');
    signal LED         : STD_LOGIC_VECTOR(15 downto 0);
    signal SEG7_CATH   : STD_LOGIC_VECTOR(6 downto 0);
    signal AN          : STD_LOGIC_VECTOR(7 downto 0);

    constant CLK_PERIOD : time := 10 ns;

begin
    uut: ElevatorController
        port map (
            CLK100MHZ => CLK100MHZ,
            BTNC => BTNC,
            BTNL => BTNL,
            BTNR => BTNR,
            SW => SW,
            LED => LED,
            SEG7_CATH => SEG7_CATH,
            AN => AN
        );

    clk_process : process
    begin
        while true loop
            CLK100MHZ <= '0';
            wait for CLK_PERIOD / 2;
            CLK100MHZ <= '1';
            wait for CLK_PERIOD / 2;
        end loop;
    end process;

    stim_proc: process
    begin
        -- reset activ
        BTNC <= '1';
        wait for 100 ns;
        BTNC <= '0';
        
        -- senzori
        BTNL <= '0'; -- not pressed
        BTNR <= '0'; -- not pressed

        wait for 100 ns;

        -- cerere interna pentru etajul 3
        -- SW(3 downto 0) = "0011" (3)
        -- SW(5 downto 4) = "01" (cerere interna)
        -- SW(6) = '1' (submit request)
        SW(3 downto 0) <= "0011";     -- etajul 3
        SW(5 downto 4) <= "01";       -- cerere interna
        SW(6) <= '1';                 -- submit
        wait for 20 ns;
        SW(6) <= '0';                 -- eliberez butonul

        -- asteptam pentru ca elevatorul sa proceseze cererea
        wait for 3 ms;

        -- final
        wait;
    end process;

end Behavioral;
