open OUnit2
open Aoc2024

let tests =
  "Day04" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 18 (Day04.count_all "day04-sample");
      let open Day04.Matrix in
      let m = create "day04" in
      assert_equal ~printer:string_of_int 140 m.width;
      assert_equal ~printer:string_of_int 140 m.height;
      assert_equal ~printer:string_of_int 2496 (Day04.count_all "day04");
    );
  ]

  let _ = run_test_tt_main tests
