# assignmnet-huffman
My work of assignmnet on *Multimedia Applications Development* course.

## Question
> Q2. Using java, implement the Huffman algorithm for encoding (`Encoder.java`)
> and decoding (`Decoder.java`) text documents. You can assume each text 
> document only contains characters with ascii code from 1 to 127. (50 marks)
>
> For encoding,
> ```
> java Encoder abc.txt abc.dat abc.dic
> ```
>
> Here, a text file called `abc.txt` will be read into memory and analyze.
> Your program should output the coding result into `abc.dat`, and output 
> the dictionary file into `abc.dic`. The `abc.dic` file saves the codeword
> for each symbol (Other information may also be included if necessary).
>
> For decoding,
> ```
> java Decoder abc.dat abc.dic abc-result.txt
> ```
>
> Here, `abc.dat` and `abc.dic` are the output of the Encoder. 
> The `abc-result.txt` is the new output text file, which should be the same 
> as `abc.txt` if correct.

(Some parts are omitted)

## Versions
- Branch `master`
  
  Final work.
  
- Branch `end-of-file`
  
  Assign a `EOF` code which is appended to the end of output file. Since it
  affect the Huffman Tree, I store the length of original file into dicitionary
  file instead.
  
- Tag `NoBuffer`
  
  A version which hasn't used `byte[]` as read/write buffers. I's slow than
  buffered version significantly when handle large files.

- Tag `NoSerialization`

  Smaller dictionary file but more complex code.

## License
[The MIT License (MIT)](http://opensource.org/licenses/MIT)
