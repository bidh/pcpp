%%%-------------------------------------------------------------------
%%% @author Biraj
%%% @copyright (C) 2018, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 17. Dec 2018 11:44 PM
%%%-------------------------------------------------------------------
-module('ScatterGather').
-export([start/0,worker/0,scatter/2,gather/1]).

%% -- COMPUTE --------------------------------------------------

n2s(N) -> lists:flatten(io_lib:format("~p", [N])). %% HACK!

seed() -> {_, A2, A3} = now(), random:seed(erlang:phash(node(), 100000),erlang:phash(A2, A3),A3).

random(N) -> random:uniform(N).

compute(X) -> random(X).

average(X,Y) -> (X + Y) / 2.

%% -- WORKER --------------------------------------------------

worker() ->
  seed(),
  receive
    split ->
      Left = 'spawn'(helloworld,worker,[]),
      Right = 'spawn'(helloworld,worker,[]),
      scatter(Left,Right);
    {compute,X,Caller} ->
      Res = compute(X),
      io:fwrite(n2s(Res) ++ "\n"),
      Caller ! {result,Res},
      worker()
  end.

%% -- SCATTER --------------------------------------------------

scatter(Left,Right) ->
  receive
    split ->
      Left ! split,
      Right ! split;
    {compute,X,Caller} ->
      Gather = 'spawn'(helloworld,gather,[Caller]),
      Left ! {compute,X,Gather},
      Right ! {compute,X,Gather}
  end,
  scatter(Left,Right).


%% -- GATHER --------------------------------------------------

gather(Caller) ->
  receive
    {result,Res1} ->
      receive
        {result,Res2} ->
          Res = average(Res1,Res2),
          Caller ! {result, Res} % die!
      end
  end.

%% -- START --------------------------------------------------

start() ->
  Worker = 'spawn'(helloworld,worker,[]),
  Worker ! split,
  Worker ! split,
  Worker ! split,
  Worker ! {compute,6,self()},
  receive
    {result,R} ->
      io:fwrite("result = " ++ n2s(R) ++ "\n")
  end.

