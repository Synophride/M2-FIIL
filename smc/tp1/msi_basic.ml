
let delay () = Thread.delay (Random.float 0.05)

let ram = ref 0
let nb_procs = 4

module Cache = struct
  type msi = M | S | I
      
  type cache = { mutable status : msi ; mutable value : int }
      
  let caches = 
    Array.init nb_procs (fun _ -> { status = I ; value = 0 }) 

  let m = Mutex.create ()
		       
  let read i =
    (*Mutex.lock m;*)
    begin
      match caches.(i).status with
      | M | S -> ()
      | I -> 
	 for j = 0 to Array.length caches - 1 do
	   delay();
	   if j = i then caches.(j).status <- S
	   else
	     if caches.(j).status = M then
	       begin
		 caches.(j).status <- S;
		 ram := caches.(j).value; (* flush *)
	       end
	 done
    end;
    (*Mutex.unlock m;*)
    caches.(i).value
	  
  let write i v = 
    (*Mutex.lock m;*)
    if caches.(i).status <> M then
      for j = 0 to Array.length caches - 1 do
	delay();
	if j = i then caches.(j).status <- M
	else caches.(j).status <- I
      done;
    caches.(i).value <- v (*;Mutex.unlock m*)
	
  let check () = 
    Mutex.lock m;
    let v = ref false in
    for i = 0 to nb_procs - 1 do
      if caches.(i).status = M then
	(if !v then raise Exit; v := true)
    done;
    Mutex.unlock m
end


let run i = 
  Format.eprintf "%d starts@." i;
  for k = 0 to 10 do
    let v = Cache.read i in
    for j = 0 to 3 do 
      ignore (Cache.read i);
      Cache.write i (v+1);
      Cache.check()
    done;
  done
  
let () = 
  let l = ref [] in
  for i = 0 to nb_procs - 1 do
    l := (Thread.create run i) :: !l;
  done;
  List.iter Thread.join !l
  

