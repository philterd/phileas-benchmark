#!/bin/bash -e

java -server -Xmx512M -XX:+AlwaysPreTouch -XX:PerBytecodeRecompilationCutoff=10000 -XX:PerMethodRecompilationCutoff=10000 -jar target/phileas-benchmark-cmd.jar all mask_all 1 15000 json > tests.jsonl

cat tests.jsonl | jq -c '. | {"index": {"_index": "phileas_benchmarks"}}, .' | curl -H "Content-Type: application/json" -XPOST localhost:9200/_bulk --data-binary @-
