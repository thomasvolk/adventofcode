open OUnit2
open Aoc2024

let tests =
  "Day04" >::: [
    "a" >:: (fun _ -> 
      let m = Day04.read_matrix "day04" in
      assert_equal ~printer:string_of_int 140 (fst (Day04.dimensions m));
      assert_equal ~printer:string_of_int 140 (snd (Day04.dimensions m));
    );
  ]

  let _ = run_test_tt_main tests
