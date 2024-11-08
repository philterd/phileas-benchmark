#!/bin/bash -e

java -server -Xmx512M -XX:+AlwaysPreTouch -XX:PerBytecodeRecompilationCutoff=10000 -XX:PerMethodRecompilationCutoff=10000 -jar target/phileas-benchmark-cmd.jar all mask_all 1 100 json > tests.jsonl

curl -X DELETE http://localhost:9200/phileas_benchmarks

curl -X PUT http://localhost:9200/phileas_benchmarks -H "Content-Type: application/json" -d'{
    "mappings": {
      "properties": {
        "calls_per_sec": {
          "properties": {
            "0": {
              "type": "long"
            },
            "1": {
              "type": "long"
            },
            "1024": {
              "type": "long"
            },
            "128": {
              "type": "long"
            },
            "1280": {
              "type": "long"
            },
            "1536": {
              "type": "long"
            },
            "16": {
              "type": "long"
            },
            "1792": {
              "type": "long"
            },
            "2": {
              "type": "long"
            },
            "2048": {
              "type": "long"
            },
            "256": {
              "type": "long"
            },
            "3072": {
              "type": "long"
            },
            "32": {
              "type": "long"
            },
            "4": {
              "type": "long"
            },
            "4096": {
              "type": "long"
            },
            "512": {
              "type": "long"
            },
            "64": {
              "type": "long"
            },
            "768": {
              "type": "long"
            },
            "8": {
              "type": "long"
            }
          }
        },
        "run_id": {
          "type": "keyword"
        },
        "document": {
          "type": "keyword"
        },
        "phileas_version": {
          "type": "keyword"
        },
        "redactor": {
          "type": "keyword"
        },
        "timestamp": {
          "type" : "date",
          "format" : "epoch_millis"
        },
        "workload_millis": {
          "type": "long"
        }
      }
    }
  }
}'

cat tests.jsonl | jq -c '. | {"index": {"_index": "phileas_benchmarks"}}, .' | curl -H "Content-Type: application/json" -XPOST localhost:9200/_bulk --data-binary @-
