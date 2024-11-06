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

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Result {

    private String document;
    private String redactor;

    @SerializedName("workload_millis")
    private long workloadMillis;

    @SerializedName("calls_per_sec")
    private Map<Integer, Long> callsPerSecond;

    public Result() {
        this.callsPerSecond = new HashMap<>();
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getRedactor() {
        return redactor;
    }

    public void setRedactor(String redactor) {
        this.redactor = redactor;
    }

    public long getWorkloadMillis() {
        return workloadMillis;
    }

    public void setWorkloadMillis(long workloadMillis) {
        this.workloadMillis = workloadMillis;
    }

    public Map<Integer, Long> getCallsPerSecond() {
        return callsPerSecond;
    }

    public void setCallsPerSecond(Map<Integer, Long> callsPerSecond) {
        this.callsPerSecond = callsPerSecond;
    }

}
