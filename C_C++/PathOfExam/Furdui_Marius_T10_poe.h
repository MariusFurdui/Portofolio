#ifndef POE_H_INCLUDED
#define POE_H_INCLUDED

void create_empty_map(int H, int W, char** a);

void print_map(int H, int W, char** a);

int find_in_map(int H, int W, char** a, char x, char rows[], int cols[]);

void encode(int H, int W, char** a, char* s);

void decode(int H, int W, char** a, char* s);

typedef struct{
	short H, A, D, s, S;
}player;

typedef struct{
	short dH, dA, dD, dS;
}item;

typedef struct{
	char type;
	char torow;
	short tocol;
}move;

typedef struct{
	player players[2];
	char s[200];
}game_state;

int next_states(int H, int W, game_state* gs, char next_player,
		    int n, item* items,
                game_state* ngs, move* moves);
void show_game_state(int H, int W, game_state* gs);
#endif // POE_H_INCLUDED
