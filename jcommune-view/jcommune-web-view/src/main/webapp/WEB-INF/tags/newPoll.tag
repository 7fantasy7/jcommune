<%--

    Copyright (C) 2011  JTalks.org Team
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/xml_rt" %>
<%@ tag body-content="empty" %>
<%@ attribute name="titleNameValue" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="pollOptionsNameValue" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="multipleName" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="multipleValue" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="endingDateNameValue" required="true" rtexprvalue="true" type="java.lang.String" %>

<div class="forum_header_table">
    <div class="forum_header">
        <span class="forum_header_answer"><spring:message code="label.poll.header"/></span>
        <span class="empty_cell"></span>
    </div>
    <ul class="forum_table" id="editPoll">
        <li class="forum_row">
            <div class="forum_answer_left">
                <spring:message code="label.poll.title"/>
            </div>
            <div class="forum_answer_right">
                <form:input path="${titleNameValue}" id="${titleNameValue}" type="text" name="${titleNameValue}"
                            size="45"
                            maxlength="255" tabindex="1"
                            class="post"/>
                <br>
                <form:errors path="${titleNameValue}" cssClass="error"/>
            </div>
        </li>
        <li class=" forum_row">
            <div class="forum_answer_left">
                <spring:message code="label.poll.options.title"/>
            </div>
            <div class="forum_answer_right">
                <form:textarea path="${pollOptionsNameValue}" rows="8"
                               class="post"/>
                <br>
                <form:errors path="${pollOptionsNameValue}" cssClass="error"/>
            </div>
        </li>
        <li class="forum_row">
            <div class="forum_answer_left">
                <spring:message code="label.poll.multiple.title"/>
            </div>
            <div class="forum_answer_right">
                <form:checkbox path="${multipleName}" id="multipleChecker"  value="${multipleValue}"/>
            </div>
        </li>

        <li class="forum_row">
            <div class="forum_answer_left">
                <spring:message code="label.poll.date"/>
            </div>
            <div class="forum_answer_right">
                <form:input path="${endingDateNameValue}" id="datepicker" type="text" readonly="true"/>
                <br>
                <form:errors path="${endingDateNameValue}" cssClass="error"/>
            </div>
        </li>
    </ul>

    <div class="forum_row" id="previewPoll">

    </div>


</div>