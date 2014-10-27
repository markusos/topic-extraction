#!/bin/bash

for f in graphs/*
do
  rm $f
done

for f in dot/*
do
  sfdp -Tsvg $f -o $f.svg
done

for f in graphs/*
do
  rm $f
done
