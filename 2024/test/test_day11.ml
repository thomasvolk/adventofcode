open OUnit2
open Aoc2024

let string_of_int_list l = List.map string_of_int l |> String.concat ", "

let tests =
  "Day07" >::: [
    "a" >:: (fun _ -> 
      assert_equal ~printer:string_of_int_list [125; 17] (Day11.Population.load "day11-sample");
      assert_equal ~printer:string_of_int_list [123; 456] (Day11.Stone.split 123456);
      assert_equal ~printer:string_of_int_list [2024] (Day11.Stone.blink 1);
      assert_equal ~printer:string_of_int_list [1; 2; 2; 2024] (Day11.Population.blink [0; 22; 1]);

      assert_equal ~printer:string_of_int_list [253000; 1; 7]
                                               (Day11.Population.blink_times 1 (Day11.Population.load "day11-sample"));
      assert_equal ~printer:string_of_int_list [2097446912; 14168; 4048; 2; 0; 2; 4; 40; 48; 2024; 40; 48; 80; 96; 2; 8; 6; 7; 6; 0; 3; 2]
                                               (Day11.Population.blink_times 6 (Day11.Population.load "day11-sample"));


      assert_equal ~printer:string_of_int 186996 (Day11.Population.load "day11" |> Day11.Population.blink_times 25 |> List.length);
    );
  ]

  let _ = run_test_tt_main tests
