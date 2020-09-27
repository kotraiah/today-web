/**
 * Original Author -> 杨海健 (taketoday@foxmail.com) https://taketoday.cn
 * Copyright © TODAY & 2017 - 2019 All Rights Reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/]
 */
package cn.taketoday.web.http;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.taketoday.context.utils.DefaultMultiValueMap;
import cn.taketoday.context.utils.MultiValueMap;

/**
 * @author TODAY <br>
 *         2020-01-30 18:31
 */
public class DefaultHttpHeaders implements HttpHeaders, MultiValueMap<String, String> {

  private static final long serialVersionUID = 1L;

  final MultiValueMap<String, String> headers;

  public DefaultHttpHeaders() {
    this(new DefaultMultiValueMap<>());
  }

  public DefaultHttpHeaders(MultiValueMap<String, String> headers) {
    this.headers = headers;
  }

  @Override
  public String getFirst(String headerName) {
    return headers.getFirst(headerName);
  }

  @Override
  public void add(String headerName, String headerValue) {
    headers.add(headerName, headerValue);
  }

  @Override
  public void addAll(MultiValueMap<String, String> values) {
    headers.addAll(values);
  }

  @Override
  public void addAll(String key, List<? extends String> values) {
    headers.addAll(key, values);
  }

  @Override
  public void set(String headerName, String headerValue) {
    headers.set(headerName, headerValue);
  }

  @Override
  public void setAll(Map<String, String> values) {
    headers.setAll(values);
  }

  @Override
  public List<String> get(Object key) {
    return headers.get(key);
  }

  @Override
  public List<String> remove(Object key) {
    return headers.remove(key);
  }

  @Override
  public int size() {
    return headers.size();
  }

  @Override
  public boolean isEmpty() {
    return headers.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return headers.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return headers.containsValue(value);
  }

  @Override
  public List<String> put(String key, List<String> value) {
    return headers.put(key, value);
  }

  @Override
  public void putAll(Map<? extends String, ? extends List<String>> m) {
    headers.putAll(m);
  }

  @Override
  public void clear() {
    headers.clear();
  }

  @Override
  public Set<String> keySet() {
    return headers.keySet();
  }

  @Override
  public Collection<List<String>> values() {
    return headers.values();
  }

  @Override
  public Set<Entry<String, List<String>>> entrySet() {
    return headers.entrySet();
  }

  @Override
  public Map<String, String> toSingleValueMap() {
    return headers.toSingleValueMap();
  }

}
