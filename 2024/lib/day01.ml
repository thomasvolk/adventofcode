open Re

let total_distance = 
  let re_npair = Perl.compile_pat "(\\d+)\\s+(\\d+)" in
  let parse_line l = 
    let g = Re.exec re_npair l in
    (
      int_of_string (Re.Group.get g 1), 
      int_of_string (Re.Group.get g 2)
    )
  in
  let pairs = 
    Io.File.read_lines "../resources/input_day01-a.txt"
    |> List.map parse_line
  in
  let n_a = pairs |> List.map fst |> List.sort compare in
  let n_b = pairs |> List.map snd |> List.sort compare in
  let sorted_pairs = List.combine n_a n_b in
  List.fold_left (fun ac (a, b) ->  ac + ( Int.abs (a - b))) 0 sorted_pairs
