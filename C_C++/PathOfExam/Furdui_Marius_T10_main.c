#include <stdio.h>
#include <stdlib.h>
#include "poe.h"
#include <string.h>

int main()
{
    ///A
//    int H=0,W=0;
//    printf("Introduceti nr. linii: ");
//    scanf("%d",&H);
//    printf("Introduceti nr. coloane: ");
//    scanf("%d",&W);
//    char** a=(char**)malloc(H*sizeof(char*));
//    for(int i=0; i<H; i++)
//    {
//        a[i]=(char*)malloc(W*sizeof(char));
//    }
//    printf("Introduceti matricea:\n");
//    for(int i=0; i<H; i++)
//    {
//        for(int j=0; j<W; j++)
//        {
//            scanf(" %c",&a[i][j]);
//        }
//    }
//    create_empty_map(H,W,a);

    ///B
//    print_map(H,W,a);

    ///C
//    char x,rows[53*100];
//    int cols[53*100];
//    getchar();
//    scanf("%c",&x);
//    int ap=find_in_map(H,W,a,x,rows,cols);
//    printf("%d\n",ap);
//    for(int i=0;i<ap;i++)
//    {
//        printf("%c ",rows[i]);
//    }
//    printf("\n");
//    for(int i=0;i<ap;i++)
//    {
//        printf("%d ",cols[i]);
//    }
//    printf("\n");
//    char* s =(char*)calloc(W*H*7,sizeof(char));
//    encode(H,W,a,s);
//    printf("Matricea codificata: ");
//    printf("%s\n",s);
//    printf("\nIntroduceti string-ul: ");
//    scanf(" %[^\n]",s);
//
//    decode(H,W,a,s);
//    printf("Harta aferenta string-ului:\n");
//    print_map(H,W,a);
    int H = 4, W = 6;
    game_state gs;
    strcpy(gs.s, "A A3 o1 B1 B C1 o2 C5");

    player p1 = {100, 40, 0, 20, 20};
    gs.players[0] = p1;
    gs.players[1] = p1;

    item items[] = {{10, 0, 0, 0}, {0, -5, 0, 0}};
    int n = 2;

    game_state ngs[10];
    move moves[10];

    int count = next_states(H, W, &gs, 'A', n, items, ngs, moves);

    printf("Stare initiala:\n");
    show_game_state(H, W, &gs);

    for (int i = 0; i < count; i++) {
        printf("\nStare %d:\n", i + 1);
        show_game_state(H, W, &ngs[i]);
        printf("Miscare: %c la %c%d\n", moves[i].type, moves[i].torow, moves[i].tocol);
    }

//    free(a);
//    free(s);
    return 0;
}
