open OUnit2
open Aoc2024

let string_of_bool_list l = l |> List.map string_of_bool |> String.concat ", "

let tests =
  "Day07" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 9 (List.length (Day07.Equation.load "day07-sample"));
      assert_equal ~printer:string_of_bool_list [true; true; true] (Day07.bits 7);
      assert_equal ~printer:string_of_bool_list [true; false; true] (Day07.bits 5);
      assert_equal ~printer:string_of_bool_list [true; false; false] (Day07.bits 4);
      assert_equal ~printer:string_of_bool_list [true] (Day07.bits 1);
      assert_equal ~printer:string_of_bool_list [false] (Day07.bits 0);
      assert_equal ~printer:string_of_bool_list [true] (Day07.bits (-1));

      assert_equal ~printer:string_of_int 3749 (Day07.sum_all_valid_equations "day07-sample");
    );
    "b" >:: (fun _ -> 
      assert_equal ~printer:string_of_int 0 0;
    )
  ]

  let _ = run_test_tt_main tests
