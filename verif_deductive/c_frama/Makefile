VERIF=frama-c
OPTIONS= -wp -wp-rte -kernel-msg-key pp -wp-debug 1
# wp-debug n 
m	: m.c
	$(VERIF) $(OPTIONS) m.c
all 	: abs max_abs max
	echo "" 
sum	: sum.c
	$(VERIF) $(OPTIONS) sum.c

max_abs	: max_abs.c
	$(VERIF) $(OPTIONS) max_abs.c

max	: max.c
	$(VERIF) $(OPTIONS) max.c

abs	: abs.c
	$(VERIF) $(OPTIONS) abs.c

