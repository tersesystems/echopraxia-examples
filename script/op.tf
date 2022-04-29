
import * as std from "std";

alias std.data as data;

library echopraxia {
  function evaluate: (string level, dict ctx) ->
	  data.any?(["ERROR","WARN","INFO"], (x) -> x == level);
}