# <a name="Home"></a> Hello World project

## Содержание:
- [Создание проекта](#CreateProject)
- [Создание приложения](#CreateApplication)
- [Запуск и деплой](#StartAndDeploy)

## [↑](#Home) <a name="CreateProject"></a> Создание проекта
Итак, нам предстоит создать Hello World проект.
Начинается всё как обычно - нам нужно быстро создать проект.
В качестве Build System, то есть системы сборки, выберем Maven.
Выбор этот обусловлен как минимум тем, что сам WildFly приводит примеры именно с Maven. Данные примеры можно увидеть тут: https://docs.jboss.org/author/display/WFLY/Getting+Started+Developing+Applications+Guide

Перейдём в каталог, в котором будем создавать подкаталог для нашего будущего проекта. Откроем в данном каталоге консоль, т.е. командную строку. В Windows можно зажать Shift, кликнуть правой кнопкой мыши в пустом месте и выбрать пункт контекстного меню: **"Открыть окно команд"**.
В окне команд пишем команду: ```mvn generate archetype```.
Подробнее про архетипы можно прочитать на сайте Maven (https://maven.apache.org/) в разделе [Archetype](https://maven.apache.org/archetype/index.html). По сути это просто создание проекта по шаблону.
Далее нам предложат выбрать id или применить фильтр.
Нас интересует ```maven-archetype-webapp``` - то есть веб-приложение.
Фильтр найдёт всего 1 шаблон. Поэтому смело вводим номер 1 и нажимаем Enter.
Выбираем самую свежую версию из указанных.
Далее необходимо указать так называемые **GAV** проекта. Подробнее можно прочитать например здесь: [Guide to naming conventions on groupId, artifactId and version](https://maven.apache.org/guides/mini/guide-naming-conventions.html).
Например:
```
groupId: com.github.vastap
artifactId: wildflyee
version: 1.0-SNAPSHOT
package: com.github.vastap
```
Подтверждаем и вуаля - у нас готов проект )
Теперь, мы можем начинать.
За пример возьмём официальный пример: [The helloworld quickstart in depth](https://github.com/wildfly/quickstart/blob/10.x/guide/HelloworldQuickstart.asciidoc#the-helloworld-quickstart-in-depth).

Но для начала нам нужно исправить наш файл POM.xml. Будем основываться на примерах самого WildFly: "[Helloworld Example](https://github.com/wildfly/quickstart/blob/master/helloworld/pom.xml)".
В блоке dependencies укажем зависимости, как это указано в helloworld от WildFly:
```xml
<!-- CDI API -->
<dependency>
	<groupId>javax.enterprise</groupId>
	<artifactId>cdi-api</artifactId>
    <scope>provided</scope>
</dependency>
<!-- Common Annotations API (JSR-250 -->
<dependency>
    <groupId>org.jboss.spec.javax.annotation</groupId>
    <artifactId>jboss-annotations-api_1.2_spec</artifactId>
    <scope>provided</scope>
</dependency>
<!-- Servlet API -->
<dependency>
    <groupId>org.jboss.spec.javax.servlet</groupId>
    <artifactId>jboss-servlet-api_3.1_spec</artifactId>
    <scope>provided</scope>
</dependency>
```

Так же по правилам хорошего тона укажем ещё и версию Java. Подробнее см. в [POM Reference](https://maven.apache.org/pom.html#Properties):
```xml
<properties>
	<maven.compiler.source>1.7</maven.compiler.source>
	<maven.compiler.target>1.7</maven.compiler.target>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>
```
На этом непосдерственное создание проекта закончилось.

## [↑](#Home) <a name="CreateApplication"></a> Создание приложения 
Далее укажем необходимые настройки и напишем несколько строчек кода.
Как ранее указано было, в [The helloworld quickstart in depth](https://github.com/wildfly/quickstart/blob/10.x/guide/HelloworldQuickstart.asciidoc#the-helloworld-quickstart-in-depth) сказано, что чтобы WildFly искал бины и включал механизм CDI, необходимо файл **beans.xml** расположить в проекте в каталоге ```src/main/webapp/WEB-INF/```.
Как должен выглядеть данный файл можно узнать из спецификации на технологию, как это обычно и бывает. Переходим на основной сайт CDI http://cdi-spec.org/ и выбираем в **"JCP Pages"** пункт CDI 2.0. Нас интересует **"Final Release"**.
Первое же совпадение по **"beans.xml"** покажет нам пример. Скопируем его:
```xml
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
            http://xmlns.jcp.org/xml/ns/javaee
            http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd">
</beans>
```

Теперь создадим каталог для исходного кода Java: ```src/main/java```
Создадим в нём пакет с названием, которое мы указали при создании Maven проекта из архетипа. Для данного примера - **com.github.vastap.wildflyee**.
Если пакет не хочется создаваться в IDE (например, в Idea), то проверьте, что каталог помечен как Source Root.
Создадим в пакете новый класс, который будет являться сервисом. Назовём его, например, **MessageService**.
Будет содержать крайне простой код:
```java
package com.github.vastap.wildflyee;

public class MessageService {

    public String createMessage(String name) {
        return "Hello " + name + "!";
    }
}
```
Теперь, рядом мы можем создать класс, являющийся сервлетом. Назовём его как-нибудь похоже. Например: **MessageServlet**. Вот так он будет выглядеть без логики:
```java
package com.github.vastap.wildflyee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Message")
public class MessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

}
```
И тут начинаются проблемы, когда "сделай как в мануале" не работает.
Проблема в том, что нужен так называемый **Bill Of Materials (BOM)**. Он указывает, какие нужно использовать версии зависимостей, в том числе для наших provided зависимостей, которые мы ожидаем от сервера.
Соответственно, добавляем BOM в наш pom.xml:
```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.wildfly.bom</groupId>
			<artifactId>wildfly-javaee7-with-tools</artifactId>
			<version>${version.jboss.bom}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```
И не забываем добавить в блок properties проперти для version.jboss.bom:
```xml
	<version.jboss.bom>11.0.0.Final</version.jboss.bom>
</properties>
```

Теперь, объявим поле с инжектом. То есть попросим WildFly используя механизм CDI предоставить нам экземпляр класса тогда, когда это потребуется:
```java
@Inject
MessageService messageService;
```

Завершим наш код тем, что допишем метод **doGet**:
```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	resp.setContentType("text/html");
	PrintWriter writer = resp.getWriter();
	writer.println("<html><head><title>Message Service</title></head><body>");
	writer.println("<h1>" + messageService.createMessage("World") + "</h1>");
	writer.println("</body></html>");
	writer.close();
}
```

Для проверки выполним в каталоге, где содержится файл pom.xml мавен команду:
```mvn clean install```

## [↑](#Home) <a name="StartAndDeploy"></a> Запуск сервера и деплой
С сайта WildFly необходимо скачать сам сервер приложений из раздела http://wildfly.org/downloads/. После извлечения файлов сервера приложений из архива в каталог мы имеем готовый к работе сервер.
Переходим в каталог **bin** и выполняем файл **standalone** (bat для Windows, sh для linux).

Дожидаемся в логе фразы вида:
```
WildFly Full 11.0.0.Final (WildFly Core 3.0.8.Final) started in 35523ms - Started 292 of 553 services (347 services are lazy, passive or on-demand)
```
Теперь из каталоге с pom.xml выполняем maven команду:
```mvn package wildfly:deploy```
Конечно же, мануал нас опять обманул ) Мы получим ошибку:
```
[ERROR] No plugin found for prefix 'wildfly' in the current project
```

Логично, что чтобы использовать плагин его нужно всё таки объявить. Так и поступаем. Добавим в pom.xml объявление плагина:
```xml
<build>
	<finalName>wildflyee</finalName>
	<plugins>
		<plugin>
			<groupId>org.wildfly.plugins</groupId>
			<artifactId>wildfly-maven-plugin</artifactId>
			<version>1.2.1.Final</version>
		</plugin>
	</plugins>
</build>
```
После этого выполняем снова ```mvn package wildfly:deploy```

В логах сервера приложений мы увидим примерно следующее:
```
WFLYUT0021: Registered web context: '/wildflyee' for server 'default-server'
WFLYSRV0010: Deployed "wildflyee.war" (runtime-name : "wildflyee.war")
```
Чтобы понять, как открыть наше приложение в логах сервера приложений так же нужно найти строку вида:
```
WFLYUT0006: Undertow HTTP listener default listening on 127.0.0.1:8080
```
и не путаем её с похожей строкой:
```
WFLYUT0006: Undertow HTTPS listener https listening on 127.0.0.1:8443
```
Т.е. ожидаем наше приложение на странице: 127.0.0.1:8080/wildflyee/Message

Ура, наше самое первое приложение заработало! )