open OUnit2
open Aoc2024

let tests =
  "Day01" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 1666427 (Day01.total_distance "day01")
    );
    "b" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 24316233 (Day01.similarity_score "day01")
    )
  ]

  let _ = run_test_tt_main tests
