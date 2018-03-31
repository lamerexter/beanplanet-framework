/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.models.tree;

import java.util.NoSuchElementException;
import java.util.Objects;

class PreorderTreeIterator<E> implements TreeIterator<E> {
    private Tree<E> tree;

    private E fromNode;
    private E nextNode;
    private E previousNode;

    public PreorderTreeIterator(Tree<E> tree) {
        this.tree = tree;
        this.fromNode = tree.getRoot();
    }

    public PreorderTreeIterator(Tree<E> tree, E fromNode) {
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
            throw new NoSuchElementException("Call to next() when there are no more pre-order nodes of this tree node. Did you first call hasNext()?");
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
            nextCandidateNode = fromNode;
        } else if ( tree.isLeaf(nextNode) ) {
            E modelRootNode = tree.getRoot();
            E currentParent = nextNode;
            while ( currentParent != null
                    && !currentParent.equals(modelRootNode)  // Do not ascend above known model root (if sub-tree)
                    && (nextCandidateNode = tree.getNextSibling(currentParent)) == null) {
                currentParent = tree.getParent(currentParent);
            }
        } else {
            nextCandidateNode = tree.getChild(nextNode, 0);
        }

        return nextCandidateNode;
    }

    private E peekPrevious() {
        if (nextNode != null) return nextNode;

        E lastCandidateNode = null;
        if (previousNode == null) {
            // First
            lastCandidateNode = fromNode;
        } else {
            lastCandidateNode = previousNode;
        }

        E modelRoot = tree.getRoot();
        if ( Objects.equals(lastCandidateNode, modelRoot) ) return null; // Not above root of sub-tree

        E previousCandidate = tree.getPreviousSibling(lastCandidateNode);
        return (previousCandidate == null ? tree.getParent(lastCandidateNode) : findLastChildOfFinalDescendentOrSelf(previousCandidate));
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
            throw new NoSuchElementException("Call to previous() when there are no more pre-order nodes of this tree node. Did you first call hasPrevious()?");
        }

        nextNode = null; // Clear any last next operation
        previousNode = previousCandidateNode;
        return previousNode;
    }

    public void set(E replacement) {
        throw new UnsupportedOperationException();
    }
}
