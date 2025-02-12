from spi import Lexer, Parser, NodeVisitor

class ListTranslator(NodeVisitor):
    def __init__(self, tree):
        self.tree = tree
        
    def visit_BinOp(self, node):
        left_node = self.visit(node.left)
        right_node = self.visit(node.right)
        
        return f'({node.op.value} {left_node} {right_node})'
    
    def visit_Num(self, node):
        return node.value
    
    def translate(self):
        return self.visit(self.tree)


def main():
    while True:
        try:
            try:
                text = input('spi> ')
            except NameError:  # Python3
                text = input('spi> ')
        except EOFError:
            break
        if not text:
            continue

        lexer = Lexer(text)
        parser = Parser(lexer)
        tree = parser.parse()
        interpreter = ListTranslator(tree)
        result = interpreter.translate()
        print(result)


if __name__ == '__main__':
    main()