CC=mpic++
FLAGS=-pedantic -Wall -Wextra -std=c++11 -fopenmp
CCOMP=g++


<<<<<<< HEAD
<<<<<<< HEAD
tp3:  tp3.cpp
	$(CCOMP) $(FLAGS) -O2 -mavx tp3.cpp -o tp3 -march=skylake

=======
>>>>>>> ccc7bd4... tp sémantique
=======
tp3:  tp3.cpp
	$(CCOMP) $(FLAGS) -O2 -mavx tp3.cpp -o tp3 -march=skylake

>>>>>>> 4e19d34... ajout vendredi
pi:	calcul-pi.cpp
	$(CCOMP) $(FLAGS) calcul-pi.cpp -o pi

bitonic: bitonic-sort.cpp
	$(CC) $(FLAGS) bitonic-sort.cpp -o bitonic

s:	pi
	./pi
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 4e19d34... ajout vendredi

r: tp3
	./tp3

r_:
<<<<<<< HEAD
=======
r:
>>>>>>> ccc7bd4... tp sémantique
=======
>>>>>>> 4e19d34... ajout vendredi
	mpirun -np 8 ./bitonic bitonic-array.txt parallel

