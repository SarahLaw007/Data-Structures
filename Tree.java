package structures;

import java.util.*;
//DRAFT 2

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
            // 2 pass, first pass is to make it in to a list
            // then, reorganize it into a tree
            root = new TagNode("root",null,null);
            TagNode ptr = root;
            while (sc.hasNextLine()) {
                ptr.sibling = new TagNode (sc.nextLine(),null,null);
                ptr = ptr.sibling;
            }
            // root = reOrganize(root, null, 0);
            reOrganize(root, null, 0);
            root = root.sibling;
	}

        private TagNode reOrganize(TagNode start, TagNode parent, int level){
            TagNode ptr = start;
            TagNode prev = null;
            int count =0;
            while(ptr!=null){
                if (ptr.tag.startsWith("</") || ptr.sibling==null) {
                    //endtag
                    ptr.tag = ptr.tag + " (count=" + count + ")";
                    ptr.tag="";

                  if (count != 0){
                        prev.sibling = null;
                 } else {
                        // empty block like <b> follow by </b> not deleting
                        // to delete uncomment below
//                        parent.firstChild = null;
                  }
                    parent.sibling=ptr.sibling;
                    ptr.sibling = null;
                    return parent;
                    
                } else if(ptr.tag.startsWith("<")){
                    //I have a tag
                    ptr.tag = ptr.tag.substring(1,ptr.tag.length()-1);
                    ptr.firstChild = ptr.sibling;
                    ptr = reOrganize(ptr.firstChild,   ptr,  level+1);
                    count++;

                } else {
                    // initially already created as sibling so nothing needed
                    count++;
                }
                prev = ptr;
                ptr = ptr.sibling;
            }
            return ptr;
        }
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		root = recursiveReplaceTag (root, oldTag, newTag);
	}
	
	
	private TagNode recursiveReplaceTag (TagNode start, String oldTag, String newTag) {
		for (TagNode ptr=start; ptr != null;ptr=ptr.sibling) {
			//I have no child but tree built so do nothing
			if (ptr.firstChild == null) {
				continue;
			} else {
				//I have a tag but what kind
				if(ptr.tag.contains(oldTag)) {
                                        // to keep all tags having a unique reference
					ptr.tag =  new String(newTag);
				}
				ptr.firstChild = recursiveReplaceTag ( ptr.firstChild,  oldTag,  newTag);
			}
		}
		return start;
		
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
            //at html
            TagNode start = root;
            // now at body
            start = start.firstChild;
            //now at child of body
            start = start.firstChild;
            recursiveBoldRow (start, row);
	}
        private void recursiveBoldRow (TagNode start, int targetRow) {
            TagNode ptr = start;
            int count = 0;
            while(ptr!=null){
                //if Match
                if(ptr.tag.equals("tr")){
                    count++;                  
                    //I'm on right tr
                    if(count==targetRow){
                      TagNode ptrTd = ptr.firstChild;
                      //begin modifying firstchild of all tds
                      //loop through the firstchild of tr as start ptr then through that start pointer's siblings
                      while(ptrTd!=null){
                          if(ptrTd.tag.equals("td")){
                              //then change that nodes first child to bold and the previous frist child to a child of bold
                              ptrTd.firstChild = new TagNode ("b", ptrTd.firstChild,null);
                          }//handled insert
                          ptrTd = ptrTd.sibling;
                      }  //while
                      break;
                    }//handled target now done
                }// not a tr
                //if not Match continue walking
                if(ptr.firstChild!=null && !ptr.tag.equals("tr")){ //child + for this excersice no check for undesired tr
                    recursiveBoldRow (ptr.firstChild, targetRow);
                }
                //either no child or wrong tr or next after recurse
                ptr = ptr.sibling;
            }   
        }

	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		recursiveRemoveTag(tag, root, null, 0);
	}
	
	private void recursiveRemoveTag(String matchTag, TagNode startNode, TagNode parentOfStart, int level) {
		TagNode ptr = startNode;
                TagNode prevSibOfPtr = null;
		while(ptr!=null) {
                    if (ptr.tag.equals(matchTag)) {
                        if(ptr.firstChild!=null){
                            //  delete and relink tree while changing li tags if necessary
                            TagNode ptrSib = ptr.firstChild;
                            TagNode lastSib = null;
                            while(ptrSib!=null){
                                if(matchTag.equals("ol")|| matchTag.equals("ul")){
                                    if(ptrSib.tag.equals("li")){
                                        ptrSib.tag = "p";
                                    }
                                }
                                lastSib = ptrSib;
                                ptrSib = ptrSib.sibling;
                            }
                            lastSib.sibling = ptr.sibling;
                            if(prevSibOfPtr!=null){
                               // use prevSibOfPtr to re-link
                               prevSibOfPtr.sibling = ptr.firstChild;
                               ptr = prevSibOfPtr.sibling;
                            } else {
                                // only call from root should have no parent
                               //make it a firstchild of parentOfStart
                               parentOfStart.firstChild = ptr.firstChild;
                               ptr = parentOfStart.firstChild;
                            }

                        }
                                
                    } else if (ptr.firstChild!=null) { //if have kids)
                        recursiveRemoveTag( matchTag, ptr.firstChild, ptr, level+1);
                        prevSibOfPtr = ptr;
                        ptr=ptr.sibling;
                    } else {
                        prevSibOfPtr = ptr;
                        ptr=ptr.sibling;
                    }
                }
		}

                
                


        private Boolean mySuperIndex(int returnResults[],String searchInHere,String searchForThis) {
            //append double space to the searchInHere to insure valid results
            String toSearch = searchInHere.toLowerCase() + "  ";
            String target = searchForThis.toLowerCase();
            //make index of searchForThis in searchInHere
            int skip=0;
            int index = toSearch.substring(skip).indexOf(target);

            while(index!=-1){
                //look at second char after index
                char endChar1 = toSearch.charAt(skip + index + target.length());
                char endChar2 = toSearch.charAt(skip + index + target.length()+ 1);
                if(endChar1 == ' '){
                    returnResults [0] = skip+index;
                    returnResults [1] = target.length();
                    returnResults [2] = skip+index + target.length();
                    return true;
                }
                if((endChar1==','|| endChar1=='!'|| endChar1=='?'|| endChar1=='.'||
                        endChar1==';'|| endChar1==':') && endChar2 == ' '){
                    returnResults [0] = skip+index;
                    returnResults [1] = target.length() + 1;
                    returnResults [2] = skip+index + target.length() + 1;
                    return true;
                }
                int newLgh = index + target.length( );

                skip += newLgh;
                index = toSearch.substring(skip).indexOf(target);
            }
            return false;
        }

        private Boolean mySuperIndex(String returnStrResults[],String searchInHere,String searchForThis) {
            int matchResults[] = new int[3];
            if (!mySuperIndex(matchResults,searchInHere,searchForThis)) {
                return false;
            }
            returnStrResults[0]= searchInHere.substring(0,matchResults[0]);
            returnStrResults[1]= searchInHere.substring(matchResults[0],matchResults[0]+matchResults[1]);
            returnStrResults[2]= searchInHere.substring(matchResults[2]);
            return true;
        }
                
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */


	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
            recurseAddTag(word, tag, root, null, 0);

           // recurseAddTag(word, tag, root, null, 0);
		
	}

        
        private void recurseAddTag(String target, String tagToAdd, TagNode startNode, TagNode parentOfStart, int level) {
            TagNode ptr = startNode;
            String lcTarget = target.toLowerCase();
            String lcCurrentTag = "";
            TagNode prevSiblingOfPointer = null;
            while(ptr!=null){
                lcCurrentTag = ptr.tag.toLowerCase();
                if (ptr.firstChild!=null){
                    //recurse to do child
                    //System.out.println("parent nothing [" + ptr.tag+"]");
                    recurseAddTag(target, tagToAdd, ptr.firstChild, ptr, level + 1);
                    prevSiblingOfPointer = ptr;
                    ptr = ptr.sibling;
                }
                else if( !lcCurrentTag.contains(lcTarget)){
                    //System.out.println("nothing [" + ptr.tag+"]");
                    prevSiblingOfPointer = ptr;
                    ptr = ptr.sibling;
                    continue;
                }else{
                    //System.out.println("something hit [" + ptr.tag+"]");
                    String strToMatch = ptr.tag;
                    TagNode potentialToAdd= new TagNode ("placeholder", null, null);
                    TagNode ptrPotentialToAdd = potentialToAdd;
                    String  returnStrResults[] = new String [3];

                    Boolean tagsFound = mySuperIndex(returnStrResults,strToMatch, lcTarget);

                    while(tagsFound){
                        TagNode before = new TagNode(returnStrResults[0],null,null);
                        TagNode tag = new TagNode (tagToAdd,null,null);
                        TagNode taggedWord = new TagNode (returnStrResults[1],null,null);
                        //special case tagged word at front
                        if(returnStrResults[0].isEmpty()){
                             ptrPotentialToAdd.sibling = tag;
                             tag.firstChild = taggedWord;
                             ptrPotentialToAdd = ptrPotentialToAdd.sibling;
                             strToMatch = returnStrResults[2]; // to do the left over string
                             tagsFound = mySuperIndex(returnStrResults,strToMatch, lcTarget);
                             continue;
                        }

                        strToMatch = returnStrResults[2]; // to do the left over string
                        //to link
                        ptrPotentialToAdd.sibling = before;
                        before.sibling = tag;
                        tag.firstChild = taggedWord;
                        //at before
                        ptrPotentialToAdd = ptrPotentialToAdd.sibling;
                        //at tag
                        ptrPotentialToAdd = ptrPotentialToAdd.sibling;
                        tagsFound = mySuperIndex(returnStrResults,strToMatch, lcTarget);


                    }
                    if(strToMatch.isEmpty()){
                        //just get rid of placeholder
                        potentialToAdd = potentialToAdd.sibling;

                    }else{
                       //get rid of placeholder and add a last tag
                        potentialToAdd = potentialToAdd.sibling;
                        TagNode lastTag = new TagNode (strToMatch,null,null);
                        ptrPotentialToAdd.sibling = lastTag;
                        ptrPotentialToAdd = ptrPotentialToAdd.sibling;
                    }

                    //begin linking the new chain to the original tree
                    //if I do not have a previous sibling
                    if(prevSiblingOfPointer == null){
                        parentOfStart.firstChild = potentialToAdd;
                        
                        prevSiblingOfPointer = ptrPotentialToAdd;
                        //changed ptr = ptrPotentialToAdd.sibling;
                        ptr = ptr.sibling;
                        continue;
                    }else{
                       prevSiblingOfPointer.sibling = potentialToAdd;
                       prevSiblingOfPointer = ptrPotentialToAdd;
                       //changed ptr = ptrPotentialToAdd.sibling;

                       ptr = ptr.sibling;
                       continue;
                    }

                    //ptr = ptr.sibling;
                }
                
                
            }
            
        }
        

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}

}
