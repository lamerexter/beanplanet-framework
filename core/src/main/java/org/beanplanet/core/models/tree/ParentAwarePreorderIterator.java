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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Objects;

class ParentAwarePreorderIterator<E> implements TreeIterator<E> {
    private Tree<E> tree;

    private Deque<E> parentStack = new ArrayDeque<>();

    private E fromNode;
    private E nextNode;
    private E previousNode;

    public ParentAwarePreorderIterator(Tree<E> tree) {
        this.tree = tree;
        this.fromNode = tree.getRoot();
    }

    public ParentAwarePreorderIterator(Tree<E> tree, E fromNode) {
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
