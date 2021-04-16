public class Token {

public int tokenType;
public int value;

public Token() {
tokenType = 0;
value = 0;
}

public Token(int tokenType, int value) {
this.tokenType = tokenType;
this.value = value;
}

public String toString() {
	return String.format("Token Type: %d Value: %d", tokenType, value);
}

}