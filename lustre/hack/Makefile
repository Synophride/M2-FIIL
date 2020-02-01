FILE=hack
NODE=

all:
	@echo  \"make NODE=n simu\"  pour simuler le noeud n
	@echo  \"make NODE=n compil\" pour generer l executabe
	@echo  \"make NODE=n verif\"  pour verifier le noeud n
	@echo  \"make NODE=n clean\"  pour supprimer les fichiers generes

simu:
	@luciole $(FILE).lus $(NODE)

compil:
	@lux $(FILE).lus $(NODE)

verif:
	@lesar $(FILE).lus $(NODE) -diag -v

clean:
	@rm -f *.ec *.oc *.o $(NODE).c $(NODE).h $(NODE)_loop.c

cleanall: clean
	@rm -f *.bak *~ $(NODE)  *.c *.h 
