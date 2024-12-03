open Core

module File = struct
  let read_lines = In_channel.read_lines
  let read_all = In_channel.read_all
end

module Resource = struct
  let read_lines name = File.read_lines ("../resources/input_" ^ name ^ ".txt")
  let read_all name = File.read_all ("../resources/input_" ^ name ^ ".txt")
end
