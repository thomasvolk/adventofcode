
let rec range i j = 
  if i > j then [] 
  else i :: range (i+1) j

module Point = struct
  type t = { x: int; y: int }
  let create x y = { x = x; y = y }
  let x t = t.x
  let y t = t.y
  let move (x, y) a = create (a.x + x) (a.y + y)
  let to_string t = "P(" ^ (string_of_int t.x) ^ "," ^ (string_of_int t.y) ^ ")"
  let inside w h t = t.x < w && t.y < h && t.x >= 0 && t.y >= 0
  let rotate w h t = create (t.x mod w) (t.y mod h)
end

module Matrix = struct

  type t = { 
    width: int;
    height: int;
    data: string list list;
  }

  let explode_string s = List.init (String.length s) (fun i -> String.get s i |> Char.escaped)

  let create src =
    let m = Io.Resource.read_lines src
    |> List.map explode_string in
    let w = List.length (List.nth m 0) in
    let h = List.length m in 
    {
      width = w;
      height = h;
      data = m;
    }

  let size t = (t.width * t.height) 

  let coordinates i t = Point.create (i mod t.width) (i / t.width) 

  let get p t = 
    match Point.inside t.width t.height p with
      | false -> None
      | true -> let row = List.nth t.data p.y in
                List.nth_opt row p.x

  let path start vec t =
    let rec walk_loop path p dp =
      match get p t with
        | Some v when Point.inside t.width t.height p -> walk_loop (path ^ v) (Point.move dp p) dp
        | _ -> path
    in
    walk_loop "" start vec

  let col x = path (Point.create x 0) (0, 1)

  let cols t = range 0 t.width |> List.map (fun x -> col x t)

  let rows t = t.data |> List.map (String.concat "") 

  let dia_tl_br t = 
      (range 1 (t.height - 1) |> List.map (fun y -> path (Point.create 0 y) (1 ,1) t))
    @ (range 0 (t.width - 1) |> List.map (fun x -> path (Point.create x 0) (1, 1) t))

  let dia_tr_bl t = 
      (range 1 (t.height - 1) |> List.map (fun y -> path (Point.create 0 y) (1 ,-1) t))
    @ (range 0 (t.width - 1)  |> List.map (fun x -> path (Point.create x (t.height - 1)) (1, -1) t))

end

let re_xmas = Re.Perl.compile_pat "XMAS"
let re_samx = Re.Perl.compile_pat "SAMX"

let count s = (Re.all re_xmas s |> List.length) + (Re.all re_samx s |> List.length)

let count_all src = 
  let m = Matrix.create src in
  (Matrix.rows m) @ (Matrix.cols m) @ (Matrix.dia_tl_br m) @ (Matrix.dia_tr_bl m)
    |> List.map count
    |> List.fold_left (+) 0
