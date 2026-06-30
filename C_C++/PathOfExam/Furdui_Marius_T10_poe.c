#include <stdio.h>
#include <stdlib.h>
#include "poe.h"
#include <string.h>

void create_empty_map(int H, int W, char** a)
{
    for(int i=0; i<H; i++)
    {
        for(int j=0; j<W; j++)
        {
            a[i][j]='.';
        }
    }
    a[0][0]='A';
    a[H-1][W-1]='B';
}

void print_map(int H, int W, char** a)
{
    printf("  ");
    for(int i=0; i<W; i++)
    {
        printf("%d ",(i+1)/10);
    }
    printf("\n");
    printf("  ");
    for(int i=0; i<W; i++)
    {
        printf("%d ",(i+1)%10);
    }
    printf("\n");
    for(int i=0; i<H; i++)
    {
        if(i<26)
        {
            printf("%c ",'A'+i);
        }
        else
        {
            printf("%c ",'a'+i-26);
        }
        for(int j=0; j<W; j++)
        {
            printf("%c ",a[i][j]);
        }
        printf("\n");
    }
}

int find_in_map(int H, int W, char** a, char x, char rows[], int cols[])
{
    int r=0;
    for(int i=0; i<H; i++)
    {
        for(int j=0; j<W; j++)
        {
            if(a[i][j]==x)
            {
                if(i<26)
                {
                    rows[r]='A'+i;
                    cols[r]=j;
                    r++;
                }
                else
                {
                    rows[r]='a'+(i-26);
                    cols[r]=j;
                    r++;
                }
            }
        }
    }
    return r;
}

///TEMA 8///
void encode(int H, int W, char** a, char* s)
{
    for(int i=0; i<H; i++)
    {
        for(int j=0; j<W; j++)
        {
            if(a[i][j]!='.')
            {
                if(a[i][j]>='0' && a[i][j]<='9')
                {
                    *s++ = 'o';
                }
                *s++ = a[i][j];
                *s++ = ' ';
                if(i>26)
                {
                    *s++ = 'A'+i+6;
                }
                else
                {
                    *s++ = 'A'+i;
                }
                if(j>9)
                {
                    *s++ = '0'+j/10;
                    *s++ = '0'+j%10+1;
                }
                else
                {
                    *s++ = '0'+j/10+1;
                }
                *s++ = ' ';
            }
        }
    }
    *s = '\0';
}

void decode(int H, int W, char** a, char* s)
{
    create_empty_map(H,W,a);
    a[0][0]='.';
    a[H-1][W-1]='.';
    while(*s != '\0')
    {
        int lin=0,col=0;
        if(*s == 'o')
        {
            if(*(s+3)>='a')
            {
                lin=*(s+3)-'a'+26;
            }
            else
            {
                lin=*(s+3)-'A';
            }
            if(*(s+5) >= '0' && *(s+5) <= '9')
            {
                col=(*(s+4)-'0')*10+(*(s+5)-'0');
                a[lin][col-1] = *(s+1);
                s+=7;
            }
            else
            {
                col=*(s+4)-'0';
                a[lin][col-1] = *(s+1);
                s+=6;
            }
        }
        else
        {
            if(*(s+2)>='a')
            {
                lin=*(s+2)-'a'+26;
            }
            else
            {
                lin=*(s+2)-'A';
            }
            if(*(s+4) >= '0' && *(s+4) <= '9')
            {
                col=(*(s+3)-'0')*10+(*(s+4)-'0');
                a[lin][col-1] = *s;
                s+=6;
            }
            else
            {
                col=*(s+3)-'0';
                a[lin][col-1] = *s;
                s+=5;
            }
        }
    }
}

int next_states(int H, int W, game_state* gs, char next_player,
                int n, item* items, game_state* ngs, move* moves)
{
    int cnt=0,nr_miscari=0;
    char** a = (char**)malloc(H * sizeof(char*));
    for (int i = 0; i < H; i++)
    {
        a[i] = (char*)malloc(W * sizeof(char));
    }
    decode(H,W,a,gs->s);
    int idplayer=next_player-'A';
    int l = -1, c = -1;
    ///aflu pozitia jucatorului curent
    for(int i = 0; i < H; i++)
    {
        for(int j = 0; j < W; j++)
        {
            if(a[i][j] == next_player)
            {
                l = i;
                c = j;
                break;
            }
        }
        if(l != -1)
            break;
    }
    game_state gs_cop=*gs;
    for(int i=0; i<H; i++)
    {
        for(int j=0; j<W; j++)
        {
            if(a[i][j]>='0' && a[i][j]<='9')
            {
                int nrob=a[i][j]-'0';
                ///actualizam proprietatile jucatorului
                gs_cop.players[idplayer].H+=items[nrob].dH;
                gs_cop.players[idplayer].A+=items[nrob].dA;
                gs_cop.players[idplayer].D+=items[nrob].dD;
                gs_cop.players[idplayer].s+=items[nrob].dS;
                ///actualizam pozitia jucatorului
                moves[nr_miscari].type = 'm';
                if(i>26)
                    moves[nr_miscari].torow='a'+i-26;
                else
                    moves[nr_miscari].torow='A'+i;
                moves[nr_miscari].tocol = j + 1;
                nr_miscari++;
                a[i][j] = next_player;
                a[l][c] = '.';
                l=i;
                c=j;
                encode(H,W,a,gs_cop.s);
                ngs[cnt]=gs_cop;
                cnt++;
            }
        }
    }
    for (int i = 0; i < H; i++) {
        free(a[i]);
    }
    free(a);
    return cnt;
}

void show_game_state(int H, int W, game_state* gs)
{
    char** a = (char**)malloc(H * sizeof(char*));
    for (int i = 0; i < H; i++)
    {
        a[i] = (char*)malloc(W * sizeof(char));
    }
    create_empty_map(H,W,a);
    decode(H,W,a,gs->s);
    print_map(H,W,a);
    for (int i = 0; i < H; i++) {
        free(a[i]);
    }
    free(a);
}


