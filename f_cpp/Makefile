CC=g++
FLAGS=-g -Wall -Wextra -pedantic --std=c++14 -Wfatal-errors -Werror

r: 	tpnote
	./tpnote

tpnote: tpnote.cpp
	$(CC) $(FLAGS) tpnote.cpp -o tpnote

tp6:	tp6.cpp
	$(CC) $(FLAGS) tp6.cpp -o tp6 

tp5:	tp5.cpp
	$(CC) $(FLAGS) tp5.cpp -o tp5 
tp3:	tp3.cpp
	$(CC) $(FLAGS) tp3.cpp -o tp3 

tp1:	tp1.cpp
	$(CC) $(FLAGS) tp1.cpp -o tp1 

tp2:	tp2.cpp
	$(CC) $(FLAGS) tp2.cpp -o tp2

clean:
	rm tp1 tp2 tp3 tp4 tp6 
