#include <stdio.h>
#include <stdlib.h>
#include "poe.h"
#include <string.h>
#include <math.h>
#include <stdbool.h>

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
    int cnt=0;
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
    for(int i=0; i<H; i++)
    {
        for(int j=0; j<W; j++)
        {
            if(a[i][j]>='0' && a[i][j]<='9')
            {
                ///copie la starea curenta
                game_state gs_cop=*gs;

                ///actualizam proprietatile jucatorului
                int nrob=a[i][j]-'0';
                gs_cop.players[idplayer].H+=items[nrob].dH;
                gs_cop.players[idplayer].A+=items[nrob].dA;
                gs_cop.players[idplayer].D+=items[nrob].dD;
                gs_cop.players[idplayer].s+=items[nrob].dS;

                ///copiem in original nr obiectului pentru a putea reveni dupa salvarea starii la harta initiala
                char original=a[i][j];

                ///mutam jucatorul
                a[i][j] = next_player;
                a[l][c] = '.';

                ///salvam mutarea
                moves[cnt].type = 'm';
                if(i>26)
                    moves[cnt].torow='a'+i-26;
                else
                    moves[cnt].torow='A'+i;
                moves[cnt].tocol = j + 1;

                ///codificam noua stare
                encode(H,W,a,gs_cop.s);

                ///salvam noua stare
                ngs[cnt]=gs_cop;
                cnt++;

                ///revenim la harta initiala
                a[i][j]=original;
                a[l][c]=next_player;
            }
        }
    }
    for(int i = 0; i < H; i++)
    {
        free(a[i]);
    }
    free(a);
    return cnt;
}

void show_game_state(int H, int W, game_state* gs)
{
    char** a = (char**)malloc(H * sizeof(char*));
    for(int i = 0; i < H; i++)
    {
        a[i] = (char*)malloc(W * sizeof(char));
    }
    decode(H,W,a,gs->s);
    print_map(H,W,a);
    for (int i = 0; i < H; i++)
    {
        free(a[i]);
    }
    free(a);
}

int sdist(char *s, char miscareL, int miscareC, char player)
{
    ///calculez stamina necesara parcurgerii unei distante
    int linP=0,colP=0;
    while(*s != '\0')
    {
        if(*s==player && *(s+1)==' ' && (*(s-1)==' ' || *(s-1)=='\0'))
        {
            if(*(s+2)>='a')
            {
                linP=*(s+2)-'a'+26;
            }
            else
            {
                linP=*(s+2)-'A';
            }
            if(*(s+4) >= '0' && *(s+4) <= '9')
            {
                colP=(*(s+3)-'0')*10+(*(s+4)-'0');
            }
            else
            {
                colP=*(s+3)-'0';
            }
            break;
        }
        else
        {
            s++;
        }
    }
    int miscarel=0;
    if(miscareL>='a')
    {
        miscarel=miscareL-'a'+26;
    }
    else
    {
        miscarel=miscareL-'A';
    }
    return abs(linP-miscarel)+abs(colP-miscareC);
}
void pozitieplayer(char *s,int* lin,int* col,char next_player)
{
    while(*s != '\0')
    {
        if(*s==next_player && *(s+1)==' ' && (*(s-1)==' ' || *(s-1)=='\0'))
        {
            if(*(s+2)>='a')
            {
                *lin=*(s+2)-'a'+26;
            }
            else
            {
                *lin=*(s+2)-'A';
            }
            if(*(s+4) >= '0' && *(s+4) <= '9')
            {
                *col=(*(s+3)-'0')*10+(*(s+4)-'0');
            }
            else
            {
                *col=*(s+3)-'0';
            }
            break;
        }
        else
        {
            s++;
        }
    }
}
bool caut_monstru(char *s,int* linM,int* colM,char player)
{
    int distantamin=20;
    int lin=0,col=0;
    int linm=0;
    int colm=0;
    while(*s != '\0')
    {
        if(*s=='m' && *(s+1)==' ' && (*(s-1)==' ' || *(s-1)=='\0'))
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
            }
            else
            {
                col=*(s+3)-'0';
            }
            if(sdist(s,lin,col,player)<distantamin)
            {
                distantamin=sdist(s,lin,col,player);
                linm=lin;
                colm=col;
            }
        }
        else
        {
            s++;
        }
    }
    if(distantamin==160)
        return false;
    else
    {
        lin=linm;
        col=colm;
        return true;
    }
}
bool castig(game_state gs,player A,player B)
{
    if(gs.players[0].A>((gs.players[1].H+gs.players[1].D)))
        return true;
    return false;
}
move best_move(const char* poe)
{
    FILE* f = fopen(poe, "r");
    ///citire dimensiuni harta si jucator actual
    int H, W;
    char next_player;
    fscanf(f, "%d %d %c", &H, &W, &next_player);
    ///citire proprietati jucatori
    game_state gs;
    player A, B;
    fscanf(f, "%hd %hd %hd %hd", &gs.players[0].H, &gs.players[0].A, &gs.players[0].D, &gs.players[0].S);
    fscanf(f, "%hd %hd %hd %hd", &gs.players[1].H, &gs.players[1].A, &gs.players[1].D, &gs.players[1].S);
    ///citire nr obiecte
    int n;
    fscanf(f, "%d", &n);
    ///citire proprietati obiecte
    item items[11];
    for(int i = 0; i < n; i++)
    {
        fscanf(f, "%hd %hd %hd %hd", &items[i].dH, &items[i].dA, &items[i].dD, &items[i].dS);
    }
    ///citire stare initiala
    fscanf(f, "%[^\n]", gs.s);

    fclose(f);

    game_state gs_nou=gs;
    game_state ngs[10];
    move moves[10];

    move best;///cea mai buna miscare
    best.type='p';
    best.torow='.';
    best.tocol=0;
    int best_score = -9999;///scorul celei mai bune miscari

    ///aflu miscarile posibile
    int nrm=next_states(H,W,&gs,next_player,n,items,ngs,moves);

    int idplayer=next_player-'A';

    ///decodific harta ca sa pot vedea spatiile libere de langa playeri/monstri
    char** a = (char**)malloc(H * sizeof(char*));
    for(int i = 0; i < H; i++)
    {
        a[i] = (char*)malloc(W * sizeof(char));
    }
    if(a == NULL)
    {
        fprintf(stderr, "eroare alocare memorie\n");
        exit(1);
    }
    decode(H,W,a,gs.s);
    print_map(H,W,a);
    ///incep sa caut cea mai buna miscare
    if(nrm>0)
    {
        ///prima data cea mai buna deplasare
        for(int i=0; i<nrm; i++)
        {
            int scor_miscare=ngs[i].players[idplayer].A+ngs[i].players[idplayer].D+ngs[i].players[idplayer].H+ngs[i].players[idplayer].s;
            if(scor_miscare>best_score && sdist(ngs[i].s,moves[i].torow,moves[i].tocol,next_player)<=ngs[i].players[idplayer].s)
            {
                best_score=scor_miscare;
                best.type='m';
                best.torow=moves[i].torow;
                best.tocol=moves[i].tocol;
            }
        }
    }
    else
    {
        ///daca pot ataca
        int linA=0,colA=0;
        pozitieplayer(gs.s,&linA,&colA,'A');
        if(gs.players[idplayer].s>=11)
        {
            ///daca pot invinge adversar
            if(castig(gs,A,B))
            {
                if(a[linA+1][colA]=='B')
                {
                    best.type='a';
                    best.torow=linA+1;
                    best.tocol=colA;
                }
                else
                {
                    if(a[linA-1][colA]=='B')
                    {
                        best.type='a';
                        best.torow=linA-1;
                        best.tocol=colA;
                    }
                    else
                    {
                        if(a[linA][colA+1]=='B')
                        {
                            best.type='a';
                            best.torow=linA;
                            best.tocol=colA+1;
                        }
                        else
                        {
                            if(a[linA][colA-1]=='B')
                            {
                                best.type='a';
                                best.torow=linA;
                                best.tocol=colA-1;
                            }
                        }
                    }
                }
            }
            else ///daca am monstru langa mine il ucid
            {
                if(a[linA+1][colA]=='m')
                {
                    best.type='a';
                    best.torow=linA+1;
                    best.tocol=colA;
                }
                else
                {
                    if(a[linA-1][colA]=='m')
                    {
                        best.type='a';
                        best.torow=linA-1;
                        best.tocol=colA;
                    }
                    else
                    {
                        if(a[linA][colA+1]=='m')
                        {
                            best.type='a';
                            best.torow=linA;
                            best.tocol=colA+1;
                        }
                        else
                        {
                            if(a[linA][colA-1]=='m')
                            {
                                best.type='a';
                                best.torow=linA;
                                best.tocol=colA-1;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            ///daca pot ucide monstru si il caut pe cel mai apropiat
            int lin=0,col=0;
            if(caut_monstru(gs.s,&lin,&col,next_player))
            {
                int scor_monstru=gs.players[idplayer].H+gs.players[idplayer].A+gs.players[idplayer].D+gs.players[idplayer].s-19-sdist(gs.s,lin,col,next_player);
                if(sdist(gs.s,lin,col,next_player)<gs.players[idplayer].s)
                {
                    ///ma duc langa monstru
                    best_score=scor_monstru;
                    best.type='m';
                    if(lin==0)
                    {
                        if(a[lin][col+1]=='.')
                        {
                            best.torow=lin;
                            best.tocol=col+1;
                        }
                        else
                        {
                            if(a[lin+1][col]=='.')
                            {
                                best.torow=lin+1;
                                best.tocol=col;
                            }
                        }
                    }
                    if(lin==H-1)
                    {
                        if(a[lin][col+1]=='.')
                        {
                            best.torow=lin;
                            best.tocol=col+1;
                        }
                        else
                        {
                            if(a[lin-1][col]=='.')
                            {
                                best.torow=lin+1;
                                best.tocol=col;
                            }
                        }
                    }
                    if(col==0)
                    {
                        if(a[lin+1][col]=='.')
                        {
                            best.torow=lin+1;
                            best.tocol=col;
                        }
                        else
                        {
                            if(a[lin][col+1]=='.')
                            {
                                best.torow=lin;
                                best.tocol=col+1;
                            }
                        }
                    }
                    if(col==W)
                    {
                        if(a[lin][col-1]=='.')
                        {
                            best.torow=lin;
                            best.tocol=col-1;
                        }
                        else
                        {
                            if(a[lin-1][col]=='.')
                            {
                                best.torow=lin-1;
                                best.tocol=col;
                            }
                        }
                    }
                }
            }
            else
            {
                ///daca ma pot duce langa inamic si la urmatoarea mea miscare il pot invinge
                if(gs.players[idplayer+1].s<3 || (gs.players[idplayer+1].A<gs.players[idplayer].H &&
                                                  gs.players[idplayer].A>(gs.players[idplayer+1].H+gs.players[idplayer+1].D) &&
                                                  (gs.players[idplayer].s-sdist(gs.s,linA,colA,'B')-1>0)))
                {
                    int linB=0,colB=0;
                    pozitieplayer(gs.s,&linB,&colB,'B');
                    best.type='m';
                    if(linB==0)
                    {
                        if(a[linB][colB+1]=='.')
                        {
                            best.torow=linB;
                            best.tocol=colB+1;
                        }
                        else
                        {
                            if(a[linB+1][colB]=='.')
                            {
                                best.torow=linB+1;
                                best.tocol=colB;
                            }
                        }
                    }
                    if(linB==H-1)
                    {
                        if(a[linB][colB+1]=='.')
                        {
                            best.torow=linB;
                            best.tocol=colB+1;
                        }
                        else
                        {
                            if(a[linB-1][colB]=='.')
                            {
                                best.torow=linB+1;
                                best.tocol=colB;
                            }
                        }
                    }
                    if(colB==0)
                    {
                        if(a[linB+1][colB]=='.')
                        {
                            best.torow=linB+1;
                            best.tocol=colB;
                        }
                        else
                        {
                            if(a[linB][colB+1]=='.')
                            {
                                best.torow=linB;
                                best.tocol=colB+1;
                            }
                        }
                    }
                    if(colB==W)
                    {
                        if(a[linB][colB-1]=='.')
                        {
                            best.torow=linB;
                            best.tocol=colB-1;
                        }
                        else
                        {
                            if(a[linB-1][colB]=='.')
                            {
                                best.torow=linB-1;
                                best.tocol=colB;
                            }
                        }
                    }
                }
                else
                {
                    ///incerc sa ma indepartez de inamic 1 patratel (daca e nevoie,
                    ///daca nu, stau pe loc, prestabilit)
                    if(sdist(gs.s,linA,colA,'B')==1)
                    {
                        if(a[linA][colA+1]=='.' && sdist(gs.s,linA,colA+1,'B')>1)
                        {
                            best.type='m';
                            best.tocol=colA+1;
                            best.torow=linA;
                        }
                        else
                        {
                            if(a[linA][colA-1]=='.' && sdist(gs.s,linA,colA-1,'B')>1)
                            {
                                best.type='m';
                                best.tocol=colA-1;
                                best.torow=linA;
                            }
                            else
                            {
                                if(a[linA+1][colA]=='.' && sdist(gs.s,linA+1,colA,'B')>1)
                                {
                                    best.type='m';
                                    best.tocol=colA;
                                    best.torow=linA+1;
                                }
                                else
                                {
                                    if(a[linA-1][colA]=='.' && sdist(gs.s,linA-1,colA,'B')>1)
                                    {
                                        best.type='m';
                                        best.tocol=colA;
                                        best.torow=linA-1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(best.torow!='.')
        {
            if(best.torow <= 26)
                best.torow = 'A' + best.torow;
            else
                best.torow = 'A' + best.torow + 6;
        }
    }
    for (int i = 0; i < H; i++)
        free(a[i]);
    free(a);
    return best;
}
