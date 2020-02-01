VERIF=frama-c
OPTIONS= -wp -wp-rte -kernel-msg-key pp -wp-debug 1 -wp-model Typed

find_min: find_min.c
	$(VERIF) $(OPTIONS) find_min.c
