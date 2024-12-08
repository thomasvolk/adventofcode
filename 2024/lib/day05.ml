
let re_rule = Re.Perl.compile_pat "(\\d+)\\|(\\d+)"

module Setup = struct 
  type t = {
    rules: (int * int) list;
    updates: int list list
  }

  let rules t = t.rules

  let updates t = t.updates

  let create src =
    let input = Io.Resource.read_all src in
    let rules =
      Re.all re_rule input
      |> List.map (fun g -> ((Re.Group.get g 1), (Re.Group.get g 2)))
      |> List.map (fun r -> ((int_of_string (fst r)), (int_of_string (snd r))))
    in
    {
      rules = rules;
      updates = []
    }
end

