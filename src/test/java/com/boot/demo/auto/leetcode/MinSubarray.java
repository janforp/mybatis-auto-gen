package com.boot.demo.auto.leetcode;

import java.util.List;

/**
 * //给你一个正整数数组 nums，请你移除 最短 子数组（可以为 空），使得剩余元素的 和 能被 p 整除。 不允许 将整个数组都移除。
 * //
 * // 请你返回你需要移除的最短子数组的长度，如果无法满足题目要求，返回 -1 。
 * //
 * // 子数组 定义为原数组中连续的一组元素。
 * //
 * //
 * //
 * // 示例 1：
 * //
 * // 输入：nums = [3,1,4,2], p = 6
 * //输出：1
 * //解释：nums 中元素和为 10，不能被 p 整除。我们可以移除子数组 [4] ，剩余元素的和为 6 。
 * //
 * //
 * // 示例 2：
 * //
 * // 输入：nums = [6,3,5,2], p = 9
 * //输出：2
 * //解释：我们无法移除任何一个元素使得和被 9 整除，最优方案是移除子数组 [5,2] ，剩余元素为 [6,3]，和为 9 。
 * //
 * //
 * // 示例 3：
 * //
 * // 输入：nums = [1,2,3], p = 3
 * //输出：0
 * //解释：和恰好为 6 ，已经能被 3 整除了。所以我们不需要移除任何元素。
 * //
 * //
 * // 示例 4：
 * //
 * // 输入：nums = [1,2,3], p = 7
 * //输出：-1
 * //解释：没有任何方案使得移除子数组后剩余元素的和被 7 整除。
 * //
 * //
 * // 示例 5：
 * //
 * // 输入：nums = [1000000000,1000000000,1000000000], p = 3
 * //输出：0
 * //
 * //
 * //
 * //
 * // 提示：
 * //
 * //
 * // 1 <= nums.length <= 10⁵
 * // 1 <= nums[i] <= 10⁹
 * // 1 <= p <= 10⁹
 * //
 * //
 * // Related Topics 数组 哈希表 前缀和 👍 133 👎 0
 *
 * @author zhucj
 * @since 20230323
 */
public class MinSubarray {

    public int minSubarray(int[] nums, int p) {
        // 1.子数组必须是原数组中的连续的一组元素
        // 2.并且在原数组中移除该子数组之后，原数组剩下的元素之和可以被p整除
        // 3.求需要被移除的子数组的最小长度

        return 0;
    }

    public static List<Integer> minSubarray(List<Integer> numList, int p) {
        if (numList == null) {
            return null;
        }
        int size = numList.size();
        for (int i = 1; i < size; i++) {
            List<Integer> list = minSubarray(numList, i, p);
            if (list != null) {
                return list;
            }
        }
        return null;
    }

    private static List<Integer> minSubarray(List<Integer> numList, int removeNum, int p) {
        return null;
    }

    private boolean isSumOfListDivisibleToP(List<Integer> list, int p) {
        int sum = 0;
        for (Integer no : list) {
            sum = sum + no;
        }
        return sum % p == 0;
    }
}
