open OUnit2
open Aoc2024

let print_list = String.concat ", "

let tests =
  "Day04" >::: [
    "a" >:: (fun _ -> 
      let m = Day04.Matrix.read "day04-sample" in
      assert_equal ~printer:Fun.id "S" (Option.get (Day04.Matrix.get (1, 1) m));
      assert_equal ~printer:print_list [] (Day04.words (5,0) m) ;
      assert_equal ~printer:string_of_int 161 (Day04.count_all "day04-sample");
    );
  ]

  let _ = run_test_tt_main tests
