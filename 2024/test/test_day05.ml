open OUnit2
open Aoc2024

let string_of_int_list l = List.map string_of_int l |> String.concat ", "

let tests =
  "Day05" >::: [
    "a" >:: (fun _ -> 
      let setup = Day05.Setup.create "day05-sample" in
      let rules = Day05.Setup.rules setup in
      let updates = Day05.Setup.updates setup in
      assert_equal ~printer:string_of_int 21 (List.length rules);
      assert_equal ~printer:string_of_int 6 (List.length updates);
      assert_equal ~printer:string_of_int_list [1] (Day05.intersection [1; 2; 3] [7; 1; 5]);
      assert_equal ~printer:string_of_int_list [1] (Day05.intersection [7; 1; 5] [1; 2; 3]);
      assert_equal ~printer:string_of_int_list [1; 2; 3] (Day05.intersection [3; 7; 1; 5; 2] [1; 2; 3]);
      assert_equal ~printer:string_of_int_list [] (Day05.intersection [] []);
      assert_equal ~printer:string_of_int_list [] (Day05.intersection [1] []);
      assert_equal ~printer:string_of_int_list [] (Day05.intersection [] [9]);
      assert_equal ~printer:string_of_int_list [] (Day05.intersection [7; 0; 5] [1; 2; 3]);
      assert_equal ~printer:string_of_int 6 (List.length (Day05.Setup.updates setup));
      assert_equal ~printer:string_of_int_list [13; 53; 29] (Day05.pages_after rules 61);
      assert_equal ~printer:string_of_int_list [97; 47; 75] (Day05.pages_before rules 61);

      assert_equal ~printer:string_of_int 143 (Day05.process_updates "day05-sample");
      assert_equal ~printer:string_of_int 5713 (Day05.process_updates "day05");
    );
  ]

  let _ = run_test_tt_main tests
