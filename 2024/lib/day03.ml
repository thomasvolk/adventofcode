open Re

let calculate_summary data =  
  let re_mul = Perl.compile_pat "mul\\((\\d+)\\,(\\d+)\\)" in
  Re.all re_mul data 
    |> List.map (fun g -> ((Re.Group.get g 1), (Re.Group.get g 2)))
    |> List.map (fun (a, b) -> ((int_of_string a), (int_of_string b)))
    |> List.map (fun (a, b) -> a * b)
    |> List.fold_left (+) 0

let get_multiplication_summary src = 
  let data = Io.Resource.read_all src in
  calculate_summary data

let get_part_after_first_occurrence pattern s =
  match Re.exec_opt pattern s with
  | Some groups ->
      let start_pos = Group.start groups 0 in
      Some (String.sub s start_pos ((String.length s) - start_pos))
  | None -> None

let get_multiplication_summary_from_active_fragments src =
  let re_do = Perl.compile_pat "do\\(\\)" in
  let re_dont = Perl.compile_pat "don\\'t\\(\\)" in
  let data = "do()" ^ (Io.Resource.read_all src) in
  Re.split re_dont data 
   |> List.map (get_part_after_first_occurrence re_do)
   |> List.map (fun p ->
       match p with
         | None -> 0
         | Some d -> calculate_summary d
    )
   |> List.fold_left (+) 0


