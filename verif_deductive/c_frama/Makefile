VERIF=frama-c
OPTIONS=-wp-rte -wp -kernel-msg-key pp

sum	: sum.c
	$(VERIF) $(OPTIONS) sum.c

all 	: abs max_abs max
	echo "" 

max_abs	: max_abs.c
	$(VERIF) $(OPTIONS) max_abs.c

max	: max.c
	$(VERIF) $(OPTIONS) max.c

abs	: abs.c
	$(VERIF) $(OPTIONS) abs.c

