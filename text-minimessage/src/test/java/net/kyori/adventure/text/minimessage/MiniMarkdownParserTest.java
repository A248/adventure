/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.minimessage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiniMarkdownParserTest {

  @Test
  public void testBold() {
    final String input = "**bold**";
    final String expected = "<bold>bold</bold>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testBold2() {
    final String input = "__bold__";
    final String expected = "<bold>bold</bold>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testItalic() {
    final String input = "*italic*";
    final String expected = "<italic>italic</italic>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testItalic2() {
    final String input = "_italic_";
    final String expected = "<italic>italic</italic>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testUnderline() {
    final String input = "~~underline~~";
    final String expected = "<underlined>underline</underlined>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testBoldWithSpaces() {
    final String input = "AaA** bold **AaA";
    final String expected = "AaA<bold> bold </bold>AaA";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testMixed() {
    final String input = "*italic*~~underline~~**bold**";
    final String expected = "<italic>italic</italic><underlined>underline</underlined><bold>bold</bold>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testMushedTogether() {
    final String input = "*a***a**";
    final String expected = "<italic>a</italic><bold>a</bold>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testWithFake() {
    final String input = "*a~*a~";
    final String expected = "<italic>a~</italic>a~";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testCrossed() {
    final String input = "*a~~*a~~";
    final String expected = "<italic>a</italic><underlined>a</underlined>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testCrossedWithSpace() {
    final String input = "*a~~ *a~~";
    final String expected = "<italic>a<underlined> </italic>a</underlined>";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testNoEnd() {
    final String input = "*a";
    final String expected = "*a";

    final String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testStripMixed() {
    final String input = "*italic*~~underline~~**bold**";
    final String expected = "italicunderlinebold";

    final String output = MiniMarkdownParser.stripMarkdown(input);

    assertEquals(expected, output);
  }

  @Test
  public void testStripMushedTogether() {
    final String input = "*a***a**";
    final String expected = "aa";

    final String output = MiniMarkdownParser.stripMarkdown(input);

    assertEquals(expected, output);
  }

  @Test
  public void testStripWithFake() {
    final String input = "*a~*a~";
    final String expected = "a~a~";

    final String output = MiniMarkdownParser.stripMarkdown(input);

    assertEquals(expected, output);
  }

  @Test
  public void testStripCrossed() {
    final String input = "*a~~*a~~";
    final String expected = "aa";

    final String output = MiniMarkdownParser.stripMarkdown(input);

    assertEquals(expected, output);
  }

  @Test
  public void testStripCrossedWithSpace() {
    final String input = "*a~~ *a~~";
    final String expected = "a a";

    final String output = MiniMarkdownParser.stripMarkdown(input);

    assertEquals(expected, output);
  }

  @Test
  public void testStripNoEnd() {
    final String input = "*a";
    final String expected = "*a";

    final String output = MiniMarkdownParser.stripMarkdown(input);

    assertEquals(expected, output);
  }
}
