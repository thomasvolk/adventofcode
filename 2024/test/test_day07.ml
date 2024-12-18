open OUnit2
open Aoc2024

let string_of_int_list l = l |> List.map string_of_int |> String.concat ", "

let tests =
  "Day07" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 9 (List.length (Day07.Equation.load "day07-sample"));
      assert_equal ~printer:string_of_int_list [1; 1; 1] (Day07.bits 3 7);
      assert_equal ~printer:string_of_int_list [1; 0; 1] (Day07.bits 3 5);
      assert_equal ~printer:string_of_int_list [1; 0; 0] (Day07.bits 3 4);
      assert_equal ~printer:string_of_int_list [1] (Day07.bits 1 1);
      assert_equal ~printer:string_of_int_list [0] (Day07.bits 1 0);
      assert_equal ~printer:string_of_int_list [1] (Day07.bits 1 (-1));

      assert_equal [[1]; [0]] (Day07.Equation.bits_map 1);

      assert_equal ~printer:string_of_int 3749 (Day07.sum_all_valid_equations "day07-sample");
      assert_equal ~printer:string_of_int 12839601725877 (Day07.sum_all_valid_equations "day07");
    );
    "b" >:: (fun _ -> 
      assert_equal ~printer:string_of_int_list [1; 1] (Day07.bits ~base:3 2 4);
      assert_equal ~printer:string_of_int_list [1; 2] (Day07.bits ~base:3 2 5);
      assert_equal ~printer:string_of_int_list [2; 0] (Day07.bits ~base:3 2 6);
      assert_equal ~printer:string_of_int_list [2; 1] (Day07.bits ~base:3 2 7);
      assert_equal ~printer:string_of_int_list [2; 2] (Day07.bits ~base:3 2 8);

      assert_equal ~printer:string_of_int 11387 (Day07.sum_all_valid_equations_with_concat "day07-sample");
      (*
      assert_equal ~printer:string_of_int 0 (Day07.sum_all_valid_equations_with_concat "day07");
      *)
    )
  ]

  let _ = run_test_tt_main tests
