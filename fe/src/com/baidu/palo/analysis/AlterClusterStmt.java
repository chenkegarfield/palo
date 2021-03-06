// Modifications copyright (C) 2017, Baidu.com, Inc.
// Copyright 2017 The Apache Software Foundation

// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.baidu.palo.analysis;

import java.util.Map;

import com.baidu.palo.common.AnalysisException;
import com.baidu.palo.common.ErrorCode;
import com.baidu.palo.common.ErrorReport;
import com.baidu.palo.common.InternalException;

public class AlterClusterStmt extends DdlStmt {

    private Map<String, String> properties;
    private String alterClusterName;
    private String clusterName;
    private int instanceNum;

    public AlterClusterStmt(String clusterName, Map<String, String> properties) {
        this.alterClusterName = clusterName;
        this.properties = properties;
    }

    @Override
    public void analyze(Analyzer analyzer) throws AnalysisException, InternalException {

        if (!analyzer.getCatalog().getUserMgr().isAdmin(analyzer.getUser())) {
            ErrorReport.reportAnalysisException(ErrorCode.ERR_CLUSTER_NO_AUTHORITY, analyzer.getUser());
        }

        if (properties == null || properties.size() == 0
                || !properties.containsKey(CreateClusterStmt.CLUSTER_INSTANCE_NUM)) {
            ErrorReport.reportAnalysisException(ErrorCode.ERR_CLUSTER_NO_PARAMETER);
        }
        try {
            instanceNum = Integer.valueOf(properties.get(CreateClusterStmt.CLUSTER_INSTANCE_NUM));
        } catch (NumberFormatException e) {
            ErrorReport.reportAnalysisException(ErrorCode.ERR_CLUSTER_NO_PARAMETER);
        }

        if (instanceNum < 0) {
            ErrorReport.reportAnalysisException(ErrorCode.ERR_CLUSTER_CREATE_ISTANCE_NUM_ERROR);
        }
    }

    @Override
    public String toSql() {
        return "ALTER CLUSTER " + alterClusterName + " PROPERTIES(\"instance_num\"=" + "\"" + instanceNum + "\")";
    }

    public int getInstanceNum() {
        return instanceNum;
    }

    public String getAlterClusterName() {
        return alterClusterName;
    }

    public void setAlterClusterName(String alterClusterName) {
        this.alterClusterName = alterClusterName;
    }

    public String getClusterName() {
        return this.clusterName;
    }

    public void setInstanceNum(int instanceNum) {
        this.instanceNum = instanceNum;
    }
}
