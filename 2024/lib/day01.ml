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
  Io.File.fold_lines "../resources/input_day01-a.txt" 0 (
    fun ac l -> 
      let t = parse_line l in
      ac + (fst t) + (snd t)
  )

