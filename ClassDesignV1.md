
```
class Question 
getMonkeys() : List 
getCorrectMonkeyId() : int 
getQuestionImage() : QuestionImage

class GameImage 
getImageData() : ByteBuffer

class Monkey extends GameImage

class QuestionImage extends GameImage

interface QuestionProvider 
createQuestion() : Question
```