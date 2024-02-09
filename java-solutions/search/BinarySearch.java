package search;

public class BinarySearch {
    /**
     * Notations: <p>
     * s[i] means parseInt(args[i])
     * <p>
     * Precondition: <p>
     * ((args[i] represent integer) for all i : 0 <= i < args.length)
     * && ((s[i] >= s[i]) for all i, j : 1 <= i < j < args.length)
     * && args.length > 0
     * <p>
     * Side effects: <p>
     * prints n
     * <p>
     * Post-condition: <p>
     * n = min {n | 1 <= n < s.length && s[n+1] <= s[0]}
     */
    public static void main(String[] args) {
        final int x = Integer.parseInt(args[0]);
        // x == s[0]
        final int[] a = new int[args.length - 1];
        // a.length + 1 == args.length
        int i = 0;
        //Cycle invariant: (a[j] == s[j+1] for all j : 0 <= j < i) && i <= a.length
        //Before cycle - i is 0, so {j : 0 <= j < i} is empty set.
        //Besides, i == 0 <= 1 - 1 == args.length - 1 == a.length; which implies i <= a.length
        while (i < a.length) {
            //Let i' = i here
            //(a[j] == s[j+1] for all j : 0 <= j < i') && i' < a.length
            a[i] = Integer.parseInt(args[i + 1]);
            //(a[j] == s[j+1] && a[i'] == s[i'+1] for all j : 0 <= j < i')
            //As such, (a[j] == s[j+1] for all j : 0 <= j <= i')
            i++; //But i' doesn't change
            //i == i' + 1
            //(a[j] == s[j+1] for all j : 0 <= j <= i') && i' < a.length
            //That is, (a[j] == s[j+1] for all j : 0 <= j <= i - 1) && i - 1 < a.length
            //That is, (a[j] == s[j+1] for all j : 0 <= j < i) && i <= a.length
        }
        //(a[j] == s[j+1] for all j : 0 <= j < i) && i <= a.length (from cycle invariant)
        //i >= a.length (negated cycle condition)
        //That is, (a[j] == s[j+1] for all j : 0 <= j < i) && i <= a.length,
        //a[j] == s[j+1] for all j : 0 <= j < a.length

        //This implies binarySearch(int, int[]) precondition because x == s[0] and a[j] == s[j+1] for all j : 0 <= j < a.length
        final int n = binarySearch(x, a);
        //Substituting x and a into binarySearch(int, int[]) post-condition we'll result in main(String[]) post-condition

        System.out.println(n); //Print n satisfying the condition
    }

    /**
     * Notations: <p>
     * a[-1] := +inf <p>
     * a[a.length] := -inf
     * <p>
     * Precondition: <p>
     * ((a[i] >= a[i]) for all i, j : 0 <= i < j < a.length)
     * Post-condition: <p>
     * n = minOrZero {n | 0 <= n < a.length && a[n] <= x}
     * Where minOrZero X := (X == \emptyset) ? 0 : min X
     */
    private static int binarySearch(int x, int[] a) {
        // n = minOrZero {n | 0 <= n < a.length && a[n] <= s[x]}
        // The same as
        // (n: 0 <= n < a.length && (a[n] <= x && (n == 0 || a[n-1] > x)), because a is nonincreasing

        int res1 = binarySearchIterative(x, a);
        int res2 = binarySearchRecursive(x, a, 0, a.length);
        if (res1 != res2) {
            throw new AssertionError();
        }
        return res1;
    }

    /**
     * Notations: <p>
     * a[-1] := +inf <p>
     * a[a.length] := -inf
     * <p>
     * Precondition: <p>
     * (a[i] >= a[j]) for all i, j : 0 <= i < j < a.length
     * <p>
     * Post-condition: <p>
     * a[R] <= x && a[R-1] > x
     */
    private static int binarySearchIterative(int x, int[] a) {
        // Such R does exist, because it's the same as taking minOrZero {...}, which is always defined.

        int left = 0;
        int right = a.length;

        //Let r : a[r] <= x && (r == 0 || a[r-1] > x)
        //Cycle invariant:
        //Exists such r : left <= r <= right
        while (left != right) {
            int mid = (left + right) / 2;
            if (x < a[mid]) {
                //a[mid] > x >= a[r], which, due to non-increase, implies mid < r
                //mid + 1 <= r
                left = mid + 1;
                //left <= r
                //Exists such r : left <= r <= right
            } else {
                //a[mid] <= x >= a[r]
                //exists such r <= right because mid \in {n | 0 <= n < a.length && a[n] <= x} by def
                right = mid;
                //Exists such r : left <= r <= right
            }
        }
        //Cycle ends, because right-left decreases with each iteration.
        //Exists such r : left <= r <= right && left == right
        //That is, r == left == right
        return left;
    }

    /**
     * Notations: <p>
     * a[-1] := +inf <p>
     * a[a.length] := -inf
     * <p>
     * Precondition: <p>
     * (a[i] >= a[j]) for all i, j : 0 <= i < j < a.length <p>
     * Exists such R (see post-condition) in left <= R <= right
     * <p>
     * Post-condition: <p>
     * left <= R <= right && a[R] <= x && (R == left || a[R-1] > x)
     */
    static int binarySearchRecursive(int x, int[] a, int left, int right) {
        int r;
        if (left == right) {
            // (Exists such R : left <= R <= right && ...) && left == right
            // That is, R == left == right
            r = left;
            // r satisfies condition for R
        } else {
            int mid = (left + right) / 2;
            if (x < a[mid]) {
                //a[mid] > x >= a[R]
                //Due to non-increase, such R > mid, R >= mid + 1
                r = binarySearchRecursive(x, a, mid + 1, right);
                //r satisfies mid + 1 <= r <= right && a[r] <= x && (r == mid + 1 || a[r-1] > x)

                // If exists R : left <= R <= right && a[R] <= x && (R == left || a[R-1] > x),
                //  it lies in x: mid+1 <= R <= right.
                // If r == mid + 1, a[r] <= x && a[r-1] == a[mid] > x, r satisfies condition for R.
                // Otherwise, a[r] <= x && a[r-1] > x, r satisfies condition for R

                //r satisfies condition for R
            } else {
                //a[mid] <= x
                //R <= mid, because mid \in {n | left <= n <= right && a[n] <= x}, mid >= 0
                // and R is minOrZero of such
                r = binarySearchRecursive(x, a, left, mid);
                //r satisfies left <= r <= mid && a[r] <= x && (r == left || a[r-1] > x)
                //r <= mid implies r <= right,
                //r satisfies condition for R
            }
            //r satisfies condition for R
        }
        //r satisfies condition for R
        return r;
    }
}
