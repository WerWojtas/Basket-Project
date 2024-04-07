# Basket Splitter
Wykonanie - Weronika Wojtas

### 1 - Wymagania
- Java 21
- JUnit-BOM v.5.9.1
- JUnit-jupiter - JUnit 5
- Json-simple v.1.1.1
- Gradle v.8.2

### 2 - Struktura programu
Program składa się z 4 klas:
- BasketSplitter - główna klasa programu, zawiera konstruktor metodę split, oraz metody pomocnicze. 
- DeliveryType - klasa zawierająca przechowywany w programime typ dostawy z jego nazwą, setem dostarczanych produktów, oraz rozmiarem.
- SubsetsChecker - klasa odpowiadająca za wielowątkowe sprawdzanie podziałów koszyka.
- BasketSplitterTest - klasa testująca dwie metody pomocnicze klasy BasketSplitter, oraz metodę split.

### 3 - Opis działania programu
Instancja klasy basket splitter przy jej zainicjowaniu tworzy wszystkie możliwe kombinacje podziałów koszyka. Przy
uruchomieniu metody split przedmioty użytkownika zamieniane są na mapy dostaw gdzie kluczem jest sposób dostawy, a 
wartością lista produktów. Mapy zamieniane są następnie na listy obiektów typu DeliveryType. Program iteruje przez
wszystkie możliwe podziały koszyka zaczynając od najmniejszych (podziału na 1 dostawcę, 2 dostawców itd.). Dla każdej grupy
podziałów wykonywane jest poszukiwanie możliwych rozwiązań wielowkątkowo. Po zakończeniu działania wątków sprawdzana jest
tablica wynikowa. W przypadku znalezienia rozwiązania zwracane jest to z najbardziej licznym dostawcom.

!! Uwagi - w przypadku przekazania pustej listy, lub braku rozwiązania (przekazany przedmiot zawiera dostawcę którego nie ma w liście dostawców)
program rzuci wyjątek RuntimeException, ponieważ dostarczona metoda split nie zawierała możliwości rzucenia wyjątku typu checked.

!! Zmiany parametrów - klasa BasketSplitter ma ustawione finalne argumenty: deliverySize, oraz threadNumber,
w przypadku chęci zmiany tych wartości należy zmienić je w klasie BasketSplitter.

### 4 - Testy
Testy zaimplementowane są za pomocą JUnit 5. W klasie BasketSplitterTest znajdują się 3 testy: testy jednostkowe funkcji
pomocniczych klasy BasketSplitter, oraz test integracyjny metody split. Aby je uruchomić należy uruchomić klasę BasketSplitterTest.