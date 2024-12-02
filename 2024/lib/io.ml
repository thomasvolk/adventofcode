open Core

module File = struct
  let read_lines = In_channel.read_lines
end

module Resource = struct
  let read_lines name = File.read_lines ("../resources/input_" ^ name ^ ".txt")
end
