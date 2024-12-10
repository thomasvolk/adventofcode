

module Setup = struct 
  type t = {
    rules: (int * int) list;
    updates: int list list
  }
  let re_rule = Re.Perl.compile_pat "(\\d+)\\|(\\d+)"
  let re_update = Re.Perl.compile_pat "(\\d+)(,\\s*\\d+)+"
  let re_int = Re.Perl.compile_pat "(\\d+)"

  let rules t = t.rules

  let updates t = t.updates

  let create src =
    let input = Io.Resource.read_all src in
    let rules =
      Re.all re_rule input
      |> List.map (fun g -> ((Re.Group.get g 1), (Re.Group.get g 2)))
      |> List.map (fun r -> ((int_of_string (fst r)), (int_of_string (snd r))))
    in
    let parse_update s = Re.all re_int s
      |> List.map (fun g -> Re.Group.get g 1) 
      |> List.map int_of_string
    in
    let updates = Re.all re_update input
      |> List.map (fun g -> (parse_update (Re.Group.get g 0)))
    in
    {
      rules = rules;
      updates = updates
    }
end

let intersection a b =
  let rec loop i l = function
    | [] -> i
    | h :: tl -> loop (i @ (List.find_all ((=) h) l)) l tl
  in
  loop [] a b

let middle_item l = List.nth l ((List.length l) / 2)

module Validation = struct
  type proposal = 
    | MustBeBefore of int
    | MustBeAfter of int
  type t = { before: int list; page: int; after: int list; proposal: proposal }
  type result = 
    | Ok
    | Invalid of t
  
  let pages_after rules page = 
    let rec get_after_loop r = function
      | [] -> r
      | (b, a) :: tl when b = page -> get_after_loop (r @ [a]) tl
      | _ :: tl -> get_after_loop r tl
    in
    get_after_loop [] rules

  let pages_before rules page = 
    let switch = List.map (fun (a, b) -> (b, a)) in
    pages_after (switch rules) page

  let validate_update rules u = 
    let rec validate_loop before = function
      | [] -> Ok
      | page :: after -> match (
                                  List.nth_opt (intersection after (pages_before rules page)) 0
                                , List.nth_opt (intersection before (pages_after rules page)) 0
                                ) with
          | (None, None) -> validate_loop (before @ [page]) after
          | (Some b, _) -> Invalid { before = before; page = page; after = after; proposal = (MustBeAfter b) }
          | (_, Some a) -> Invalid { before = before; page = page; after = after; proposal = (MustBeBefore a) }
    in
    validate_loop [] u
end

let process_updates src =
  let setup = Setup.create src in
  let rules = Setup.rules setup in
  Setup.updates setup
    |> List.map (fun u ->
        let mi = middle_item u in
        match Validation.validate_update rules u with
          | Ok -> Some mi
          | _ -> None
      )
    |> List.filter Option.is_some
    |> List.map Option.get
    |> List.fold_left (+) 0

