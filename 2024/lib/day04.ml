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

let collect d x y matrix =
  let (w, _h) = dimensions matrix in
  let rec collect_loop a n get_pos =
    let (x, y) = get_pos n in
    match get x y matrix with
      | None -> a
      | Some v -> collect_loop (a ^ v) (n + 1) get_pos
  in
  match d with
    | Horizontal -> collect_loop "" 0 (fun n -> (x + n, y))
    | Vertical -> collect_loop "" 0 (fun n -> (x, y + (w * n)))
    | TopLeftBottomRight -> collect_loop "" 0 (fun n -> (x + n , y + w))
    | TopRightBottomLeft -> collect_loop "" 0 (fun n -> (x - n , y + w))

let re_xmas = Perl.compile_pat "XMAS"
let re_samx  = Perl.compile_pat "SAMX"

let count s = 
  let xmas_cnt = Re.all re_xmas s |> List.length in
  let samx_cnt = Re.all re_samx s |> List.length in
  xmas_cnt + samx_cnt

let collect_and_count d x y matrix = collect d x y matrix |> count

let count_trough_all_paths matrix =
  let (w, h) = dimensions matrix in
  let rec walk_down ?(stop = 0) c i f =
    match i with
     | i when i < stop -> c
     | _ -> walk_down ~stop:stop (c + (f i)) (i - 1) f
  in
  let cnt_rows = walk_down 0 (h - 1) (fun y -> collect_and_count Vertical 0 y matrix) in
  let cnt_cols = walk_down 0 (w - 1) (fun x -> collect_and_count Horizontal x 0 matrix) in
  let cnt_tlbr_y = walk_down ~stop:1 0 (h - 1) (fun y -> collect_and_count TopLeftBottomRight 0 y matrix) in
  let cnt_tlbr_x = walk_down 0 (w - 1) (fun x -> collect_and_count TopLeftBottomRight x 0 matrix) in
  let cnt_trbl_y = walk_down ~stop:1 0 (h - 1) (fun y -> collect_and_count TopRightBottomLeft 0 y matrix) in
  let cnt_trbl_x = walk_down 0 (w - 1) (fun x -> collect_and_count TopRightBottomLeft x 0 matrix) in
  cnt_cols + cnt_rows + cnt_tlbr_x + cnt_tlbr_y + cnt_trbl_x + cnt_trbl_y

