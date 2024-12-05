open OUnit2
open Aoc2024

let print_list = String.concat ", "

let tests =
  "Day04" >::: [
    "a" >:: (fun _ -> 
      let m = Day04.Matrix.read "day04-sample" in
      assert_equal ~printer:Fun.id "S" (Option.get (Day04.Matrix.get (1, 1) m));
      assert_equal ~printer:Day04.Point.to_string (Day04.Point.create 1 1) (Day04.Point.add (Day04.Point.create 0 0) (Day04.Point.create 1 1));
      assert_equal ~printer:Day04.Point.to_string (Day04.Point.create (-1) 1) (Day04.Point.add (Day04.Point.create 8 0) (Day04.Point.create (-9) 1));
      assert_equal ~printer:print_list ["X"; "X"; "XMAS"; "XSAM"; "XMMS"; "XXSA"; "XXSM"; "X"] (Day04.words (5,0) m) ;
      assert_equal ~printer:string_of_int 1 (Day04.count (5,0) m) ;
      assert_equal ~printer:string_of_int 2 (Day04.count (3,4) m) ;
      assert_equal ~printer:string_of_int 18 (Day04.count_all "day04-sample");
    );
  ]

  let _ = run_test_tt_main tests
