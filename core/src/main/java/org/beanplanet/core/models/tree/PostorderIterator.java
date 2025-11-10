/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beanplanet.core.models.tree;

import java.util.NoSuchElementException;

public class PostorderIterator<E> implements TreeIterator<E> {
    private Tree<E> tree;

    private E fromNode;
    private E nextNode;
    private E previousNode;

    public PostorderIterator(Tree<E> tree) {
        this.tree = tree;
        this.fromNode = tree.getRoot();
    }

    public PostorderIterator(Tree<E> tree, E fromNode) {
        this.tree = tree;
        this.fromNode = fromNode;
    }

    public Tree<E> getTree() {
        return tree;
    }

    public E getFromNode() {
        return fromNode;
    }

    public boolean hasNext() {
        return peekNext() != null;
    }

    public E next() {
        E nextCandidateNode = peekNext();
        if (nextCandidateNode == null) {
            throw new NoSuchElementException("Call to next() when there are no more post-order nodes of this tree node. Did you first call hasNext()?");
        }

        previousNode = null; // Clear any last previous operation
        this.nextNode = nextCandidateNode;
        return this.nextNode;
    }

    private E peekNext() {
        if (previousNode != null) return previousNode;

        E nextCandidateNode = null;
        if (nextNode == null) {
            // First
            nextCandidateNode = findLeftmostDecendentOrSelf(fromNode);
        } else {
            if ( !nextNode.equals(tree.getRoot()) ) {
                nextCandidateNode = tree.getNextSibling(nextNode);
                if (nextCandidateNode != null) {
                    nextCandidateNode = findLeftmostDecendentOrSelf(nextCandidateNode);
                } else {
                    if (nextNode.equals(tree.getRoot())) {
                        nextCandidateNode = null; // Do not ascend above known model root (if sub-tree)
                    } else {
                        nextCandidateNode = tree.getParent(nextNode);
                    }
                }
            }
        }

        return nextCandidateNode;
    }

    private E findLeftmostDecendentOrSelf(E candidateNode) {
        if (candidateNode == null) return null;

        while ( !tree.isLeaf(candidateNode) ) {
            candidateNode = tree.getChild(candidateNode, 0);
        }

        return candidateNode;
    }

    private E peekPrevious() {
        if (nextNode != null) return nextNode;

        E previousCandidateNode = null;
        E modelRootNode = tree.getRoot();
        if (previousNode == null) {
            // First
//            if ( !modelRootNode.equals(fromNode) ) {
                previousCandidateNode = fromNode;
//            }
        } else if ( tree.isLeaf(previousNode) ){
            previousCandidateNode = previousNode;
            E ancestorSibling = null;
            while ( (previousCandidateNode = tree.getParent(previousCandidateNode)) != null
                    && ancestorSibling == null) {
                if ( previousCandidateNode.equals(modelRootNode) ) {
                    return null;    // Do not ascend above known model root (if sub-tree)
                }
                ancestorSibling = tree.getPreviousSibling(previousCandidateNode);
            }
            previousCandidateNode = ancestorSibling;
        } else {
            previousCandidateNode = tree.getChild(previousNode, tree.getNumberOfChildren(previousNode)-1);
        }

        return previousCandidateNode;
    }

    private E findLastChildOfFinalDescendentOrSelf(E previousCandidate) {
        while ( !tree.isLeaf(previousCandidate) ) {
            previousCandidate = tree.getChild(previousCandidate, tree.getNumberOfChildren(previousCandidate)-1);
        }

        return previousCandidate;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasPrevious() {
        return peekPrevious() != null;
    }

    public E previous() {
        E previousCandidateNode = peekPrevious();
        if (previousCandidateNode == null) {
            throw new NoSuchElementException("Call to previous() when there are no more post-order nodes of this tree node. Did you first call hasPrevious()?");
        }

        nextNode = null; // Clear any last next operation
        previousNode = previousCandidateNode;
        return previousNode;
    }

    public void set(E replacement) {
        throw new UnsupportedOperationException();
    }
}
