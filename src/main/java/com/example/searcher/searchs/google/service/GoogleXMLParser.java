package com.example.searcher.searchs.google.service;

import com.example.searcher.searchs.google.exceptions.GoogleXmlParseException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GoogleXMLParser {

	private static final String PARENT_TAG = "grouping";
	private static final String CHILD_TAG = "group";
	private static final String DOC_TAG = "doc";
	private static final String DOMAIN_TAG = "domain";

	/**
	 * Объявляем приватный конструктор чтоб не было соблазна создавать инстансы
	 */
	private GoogleXMLParser() {

	}

	/**
	 * Ищет строку закленные в тег DOMAIN_TAG
	 *
	 * @param xml - входной xml файл
	 * @return Список найденных доменов
	 */
	protected static List<String> getDomainList(String xml) {

		Document document;

		try {
			document = getDocumentBuilder().parse(toInputStream(xml));
		} catch (SAXException | IOException ex) {
			throw new GoogleXmlParseException("Element with name of GROUPING not exists");
		}

		// Ищем родительский для всех найденных доменов
		NodeList parentTagNode = document.getElementsByTagName(PARENT_TAG);

		// если родителя с заданным именем не найдено, то бросаем исключение
		if (parentTagNode.getLength() == 0) {
			throw new GoogleXmlParseException("Element with name of GROUPING not exists");
		}

		// получаем список всех дочерних элементов
		NodeList childNodes = parentTagNode.item(0).getChildNodes();

		List<String> result = new ArrayList<>();

		// проходимся по всем элементам и получаем значение ключа DOMAIN_TAG
		for (int i = 0; i < childNodes.getLength(); i++) {

			try {
				Node node = childNodes.item(i);

				if (!CHILD_TAG.equals(node.getNodeName())) {
					continue;
				}

				Node docNode = getNodeByName(node.getChildNodes(), DOC_TAG)
					.orElseThrow(() -> new GoogleXmlParseException("Doc node not exists"));

				Node domainNode = getNodeByName(docNode.getChildNodes(), DOMAIN_TAG)
					.orElseThrow(() -> new GoogleXmlParseException("Domain node not exists"));

				String domain = domainNode.getTextContent().trim();
				result.add(domain);

			} catch (GoogleXmlParseException ex) {
				return Collections.emptyList();
			}
		}

		return result;
	}

	private static InputStream toInputStream(String xml) {
		//Входную строку содержащую xml упаковываем в inputSource, затем парсим ее
		return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Возвращает узел с заданным именем из списка узлов
	 *
	 * @param list список узлов
	 * @param name наименование искомого узла
	 * @return Найденный узел в Optional
	 * @throws GoogleXmlParseException
	 */
	private static Optional<Node> getNodeByName(NodeList list, String name) throws GoogleXmlParseException {

		if (Objects.isNull(list) || Objects.isNull(name)) {
			throw new GoogleXmlParseException("Not valid xml");
		}

		for (int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			if (name.equals(item.getNodeName())) {
				return Optional.of(item);
			}
		}
		return Optional.empty();
	}

	private static DocumentBuilder getDocumentBuilder() throws GoogleXmlParseException {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			return documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new GoogleXmlParseException("Filed parse xml");
		}
	}
}
