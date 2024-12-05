open Re

let explode_string s = List.init (String.length s) (fun i -> String.get s i |> Char.escaped)

let read_matrix src =
  Io.Resource.read_lines src
   |> List.map explode_string

type direction = Horizontal | Vertical | TopLeftBottomRight | TopRightBottomLeft

let dimensions matrix = 
  let w = List.length (List.nth matrix 0) in
  let h = List.length matrix in 
  (w, h)

let get x y matrix = 
  match List.nth_opt matrix y with
    | None -> None
    | Some r -> match List.nth_opt r x with
      | None -> None
      | v -> v

let collection d x y matrix =
  let (w, _h) = dimensions matrix in
  let rec collect a n get_pos =
    let (x, y) = get_pos n in
    match get x y matrix with
      | None -> a
      | Some v -> collect (a ^ v) (n + 1) get_pos
  in
  match d with
    | Horizontal -> collect "" 0 (fun n -> (x + n, y))
    | Vertical -> collect "" 0 (fun n -> (x, y + (w * n)))
    | TopLeftBottomRight -> collect "" 0 (fun n -> (x + n , y + w))
    | TopRightBottomLeft -> collect "" 0 (fun n -> (x - n , y + w))

let xmas_count src =
  let matrix = read_matrix src in
  let (w, h) = dimensions matrix in
  let re_xmas = Perl.compile_pat "XMAS" in
  let re_samx  = Perl.compile_pat "SAMX" in
  let count s = 
    let xmas_cnt = Re.all re_xmas s |> List.length in
    let samx_cnt = Re.all re_samx s |> List.length in
    xmas_cnt + samx_cnt
  in
  Error "not implemented"

