/*
 *     Copyright 2024 Philterd, LLC @ https://www.philterd.ai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.philterd.phileas.benchmark;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Run benchmark workloads for Phileas PII engine.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // show usage statement if needed
        if (args.length != 4 && args.length != 5) {
            System.out.println("Usage: java ai.philterd.phileas.benchmark.Main <document> <redactor> <repetitions> <workload_millis> <output_format>");
            throw new IllegalArgumentException("Invalid arguments");
        }

        // read arguments
        final String arg_document = args[0];
        final String arg_redactor = args[1];
        final int repetitions = Integer.parseInt(args[2]);
        final int workload_millis = Integer.parseInt(args[3]);

        String arg_format = "sysout";
        if(args.length == 5) {
            arg_format = args[4];
        }

        // create redactor based on Phileas PII engine
        final Redactor redactor = new Redactor(arg_redactor);

        final List<Result> results = new LinkedList<>();

        // repeatedly redact documents and print results
        final List<String> documents = "all".equals(arg_document) ? Documents.keys : List.of(arg_document);
        final int[] value_lengths = {0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 768, 1024, 1280, 1536, 1792, 2048, 3072, 4096};

        for (int i = 0; i < repetitions; i++) {

            for (final String document : documents) {

                if (!arg_format.equals("json")) {
                    System.out.println("\n------------------------------------------------------------------------------------------");
                    System.out.println("Using document: " + document);
                    System.out.println("Using redactor: " + arg_redactor);
                    System.out.println("Using workload_millis: " + workload_millis);
                    System.out.println("\nstring_length,calls_per_sec");
                }

                final Map<Integer, Long> calls = new HashMap<>();

                for (int value_length : value_lengths) {

                    if(Documents.get(document).length() >= value_length) {

                        final String value = Documents.get(document).substring(0, value_length);
                        final long calls_per_sec = run_workload(workload_millis, redactor, value);

                        if (!arg_format.equals("json")) {
                            System.out.println(value.length() + "," + calls_per_sec);
                        }

                        calls.put(value_length, calls_per_sec);

                    } else {
                        break;
                    }

                }

                final Result result = new Result();
                result.setWorkloadMillis(workload_millis);
                result.setRedactor(arg_redactor);
                result.setDocument(document);
                result.setCallsPerSecond(calls);

                results.add(result);

            }

        }

        if(arg_format.equals("json")) {
            final Gson gson = new Gson();
            System.out.println(gson.toJson(results));
       }

    }

    private static long run_workload(int millis, Redactor redactor, String value) throws Exception {

        final long start = System.currentTimeMillis();
        long calls = -1;
        while ((++calls % 100 != 0) || (System.currentTimeMillis() - start < millis)) redactor.filter(value);

        return calls * 1000 / (System.currentTimeMillis() - start);

    }

}
