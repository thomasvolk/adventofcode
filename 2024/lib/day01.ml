open Re

let get_columns = 
  let re_npair = Perl.compile_pat "(\\d+)\\s+(\\d+)" in
  let parse_line l = 
    let g = Re.exec re_npair l in
    (
      int_of_string (Re.Group.get g 1), 
      int_of_string (Re.Group.get g 2)
    )
  in
  let pairs = 
    Io.File.read_lines "../resources/input_day01.txt"
    |> List.map parse_line
  in
  let c_l = pairs |> List.map fst |> List.sort compare in
  let c_r = pairs |> List.map snd |> List.sort compare in
  (c_l, c_r)

let total_distance = 
  let (l, r) = get_columns in
  let sorted_pairs = List.combine l r in
  List.fold_left (fun ac (a, b) ->  ac + ( Int.abs (a - b))) 0 sorted_pairs

let similarity_score =
  let (l, r) = get_columns in
  let ul = List.sort_uniq compare l in
  ul 
    |> List.map (fun n -> n * (r |> List.find_all ((=) n) |> List.length))
    |> List.fold_left (+) 0



