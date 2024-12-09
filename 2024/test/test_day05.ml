open OUnit2
open Aoc2024

let tests =
  "Day05" >::: [
    "a" >:: (fun _ -> 
      let setup = Day05.Setup.create "day05-sample" in
      assert_equal ~printer:string_of_int 21 (List.length (Day05.Setup.rules setup));
      assert_equal ~printer:string_of_int 6 (List.length (Day05.Setup.updates setup));
      assert_equal ~printer:string_of_bool true (Day05.have_same_items [1; 2; 3] [7; 1; 5]);
      assert_equal ~printer:string_of_bool true (Day05.have_same_items [7; 1; 5] [1; 2; 3]);
      assert_equal ~printer:string_of_bool false (Day05.have_same_items [] []);
      assert_equal ~printer:string_of_bool false (Day05.have_same_items [1] []);
      assert_equal ~printer:string_of_bool false (Day05.have_same_items [] [9]);
      assert_equal ~printer:string_of_bool false (Day05.have_same_items [7; 0; 5] [1; 2; 3]);
    );
  ]

  let _ = run_test_tt_main tests
